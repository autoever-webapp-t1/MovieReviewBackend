package com.movie.MovieReview.sse.controller;

import com.movie.MovieReview.sse.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
public class SseController {
  private final SseService sseService;

  public SseController(SseService sseService) {
      this.sseService = sseService;
  }

  @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter subscribe() {
      return sseService.subscribe();
  }

  @PostMapping("/notify")
  }
}
