package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.sse.dto.MessageDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class SseService {
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();

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
//    @Scheduled(cron = "0 0 21 3/14 * ") // At 09:00 PM, every 14 days, starting on day 3 of the month
    public void notify(MessageDto messageDto) {
        String message = messageDto.getMessage();
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
