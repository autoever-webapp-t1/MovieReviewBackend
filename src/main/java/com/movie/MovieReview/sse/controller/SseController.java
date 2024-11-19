package com.movie.MovieReview.sse.controller;

import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.sse.dto.MessageDto;
import com.movie.MovieReview.sse.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SseController {
    private final SseService sseService;

    @Autowired
    private JwtTokenService jwtTokenService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    //JWTToken에서 memberId추출
    private Long extractMemberId(String authorizationHeader) throws Exception {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("클라이언트에서 헤더 토큰 오류!!!!!");
        }

        String token = authorizationHeader.substring(7); // JWT 토큰 뽑아내기
        if (!jwtTokenService.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰!!!!");
        }

        return Long.valueOf(jwtTokenService.getPayload(token));
    }

    //클라이언트와 서버 연결
//    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribe(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        return sseService.subscribe(userPrincipal.getId());
//    }

    //sse with jwtToken
//    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribe(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
//        Long memberId = extractMemberId(authorizationHeader);
//        return sseService.subscribe(memberId);
//    }

    //test with pathvariable
    @GetMapping(value = "/events/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("memberId") Long memberId) {
        return sseService.subscribe(memberId);
    }

    @PostMapping("/notify")
    public void notify(@RequestBody MessageDto messageDto) {
        sseService.setMessage(messageDto);
    }


}