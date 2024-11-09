package com.movie.MovieReview.sse.controller;

import com.movie.MovieReview.sse.dto.MessageDto;
import com.movie.MovieReview.sse.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://ec2-43-201-114-161.ap-northeast-2.compute.amazonaws.com/")
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
  public void notify(@RequestBody MessageDto messageDto) {
      sseService.notify(messageDto);
  }
}
