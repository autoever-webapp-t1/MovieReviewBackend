package com.movie.MovieReview.sse.controller;

import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.sse.dto.MessageDto;
import com.movie.MovieReview.sse.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return sseService.subscribe(userPrincipal.getId());
    }

    @PostMapping("/notify")
    public void notify(@RequestBody MessageDto messageDto) {
        sseService.setMessage(messageDto);
    }


}
