package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.sse.dto.MessageDto;
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
    private static final Logger log = LoggerFactory.getLogger(SseServiceTest.class);// 30분
    SseService sseService = new SseService();
    @Test
    void subscribe() {
        String emitter = sseService.subscribe().toString();
        log.info("emitter: " + emitter);
    }

    @Test
    void setMessage() {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("test");
        log.info("message: "+messageDto.getMessage());
        sseService.setMessage(messageDto);
    }

    @Test
    void alarm() {
    }
}