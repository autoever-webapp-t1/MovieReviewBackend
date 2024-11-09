package com.movie.MovieReview.sse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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

            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
            }
        }
    }
}
