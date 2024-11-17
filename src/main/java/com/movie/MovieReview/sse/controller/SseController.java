package com.movie.MovieReview.sse.controller;

import com.movie.MovieReview.sse.dto.MessageDto;
import com.movie.MovieReview.sse.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SseController {
    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    //클라이언트와 서버 연결
    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        return sseService.subscribe();
    }

    @PostMapping("/notify")
    public void notify(@RequestBody MessageDto messageDto) {
        sseService.setMessage(messageDto);
    }
}
