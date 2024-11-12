package com.movie.MovieReview.sse.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SseServiceTest {

    @Test
    void subscribe() {
        SseEmitter se = new SseEmitter(60 * 1000L); // 30분


    }

    @Test
    void setMessage() {
    }

    @Test
    void alarm() {
    }
}