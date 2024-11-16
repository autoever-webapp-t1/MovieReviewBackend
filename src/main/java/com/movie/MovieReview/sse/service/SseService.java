package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.sse.dto.MessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@Log4j2
public class SseService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private UserPrincipal userPrincipal;
    private MemberRepository memberRepository;
    private String loginMemberName;
    private final LinkedBlockingQueue<MessageDto> messageQueue = new LinkedBlockingQueue<>();
    private SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    public SseEmitter subscribe(String memberName) {
        MemberEntity member = memberRepository.findByEmail(memberName).orElseThrow(()->new RuntimeException("no member"));
        Long memberId = member.getMemberId();
        loginMemberName = memberName;
        emitters.put(memberId, emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected!"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        emitter.onCompletion(()->emitters.remove(emitter));
        emitter.onTimeout(()->emitters.remove(emitter));
        emitter.onError((e)->emitters.remove(emitter));

        return emitter;
    }

    public void setMessage(MessageDto messageDto) {
        messageQueue.offer(messageDto);
    }
//        @Scheduled(cron = "0 0 21 ? * MON") // At 09:00 PM, 매주 목요일 실행
    @Scheduled(cron = "0 * * * * *") // 매분 0초에 실행(테스트용)
    public void alarm() {
        MemberEntity member = memberRepository.findByEmail(loginMemberName).orElseThrow(()->new RuntimeException("no member"));
        Long memberId = member.getMemberId();
        MessageDto messageDto = messageQueue.poll();
        if (messageDto != null) {
            String message = messageDto.getMessage();
            log.info("message: " + message);
            emitter = emitters.get(memberId);
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().data(message));
                } catch (IOException e) {
                    emitters.remove(memberId);
                }
            }

        }
    }

}


