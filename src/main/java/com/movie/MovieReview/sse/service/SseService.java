package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.sse.dto.MessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@Log4j2
public class SseService {
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<MessageDto> messageQueue = new LinkedBlockingQueue<>();
    private SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);


    public SseEmitter subscribe(Long memberId) {
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

    // 게시글에 댓글 달렸을 때 알람 기능 구현
    public void sendNotification(Long memberId, String message) {
        SseEmitter emitter1 = emitters.get(memberId);
        if (emitter1 != null) {
            try {
                emitter.send(SseEmitter.event().name("NEW_COMMENT").data(message));
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        }
    }

    //        @Scheduled(cron = "0 0 21 ? * MON") // At 09:00 PM, 매주 목요일 실행
    @Scheduled(cron = "0 * * * * *") // 매분 0초에 실행(테스트용)
    public void alarm() {
        MessageDto messageDto = messageQueue.poll();
        if (messageDto != null) {
            String message = messageDto.getMessage();
            log.info("message: "+ message);
            Iterator<SseEmitter> iterator = emitters.values().iterator();
            while (iterator.hasNext()) {
                SseEmitter emitter = iterator.next();
                try {
                    emitter.send(SseEmitter.event().data(message));
                } catch (IOException e) {
                    iterator.remove();
                }
            }
        }
    }

}


