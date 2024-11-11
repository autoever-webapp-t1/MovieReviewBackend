package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.sse.dto.MessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@Log4j2
public class SseService {
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();

    private final LinkedBlockingQueue<MessageDto> messageQueue = new LinkedBlockingQueue<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected!"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        emitters.add(emitter);
        emitter.onCompletion(()->emitters.remove(emitter));
        emitter.onTimeout(()->emitters.remove(emitter));
        emitter.onError((e)->emitters.remove(emitter));

        return emitter;
    }

    public void setMessage(MessageDto messageDto) {
        messageQueue.offer(messageDto);
    }
        @Scheduled(cron = "0 0 21 ? * TUE *") // At 09:00 PM, 매주 목요일 실행
//    @Scheduled(cron = "0 * * * * *") // 매분 0초에 실행
    public void alarm() {
        MessageDto messageDto = messageQueue.poll();
        if (messageDto != null) {
            String message = messageDto.getMessage();
            log.info("message: "+ message);
            Iterator<SseEmitter> iterator = emitters.iterator();
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
