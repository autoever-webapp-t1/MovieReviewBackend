package com.movie.MovieReview.sse.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SseServiceTest {
    private static final Logger log = LoggerFactory.getLogger(SseServiceTest.class);
    SseEmitter se = new SseEmitter(60 * 1000L); // 30분
    SseService sseService = new SseService();
    @Test
    void subscribe() {
        sseService.subscribe();
        log.info("emitter's timeout " +sseService.emitter);
    }

    @Test
    void setMessage() {
    }

    @Test
    void alarm() {
    }
}