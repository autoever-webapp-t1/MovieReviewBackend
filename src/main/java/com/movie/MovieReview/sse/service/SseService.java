package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.sse.dto.MessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private MemberEntity getLoginMember() {
        String loginMemberEmail = userPrincipal.getEmail();
        return memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    private final LinkedBlockingQueue<MessageDto> messageQueue = new LinkedBlockingQueue<>();
    private SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    public SseEmitter subscribe() {
        Long memberId = getLoginMember().getMemberId();
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
            log.warn("사용자 인증정보가 없습니다");
            return;
        }
        UserPrincipal userPrincipal1 = (UserPrincipal) authentication.getPrincipal();
        Long memberId = userPrincipal1.getId();
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
            } else  {
                log.warn("이 사용자에 대한 emitter가 존재하지 않습니다. userId: {}", memberId);
            }
        }
    }

}


