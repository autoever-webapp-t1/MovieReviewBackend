package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.member.service.MemberService;
import com.movie.MovieReview.member.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

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

    @GetMapping("/info")
    public MemberDto info(){
        final long memberId = SecurityUtil.getCurrentUserId();
        MemberDto memberDto = memberService.findById(memberId);
        if(memberDto == null){
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }
        return memberDto;
    }

    @PutMapping("/{memberId}/nickname")
    public ResponseEntity<?> updateNickname(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam String newname) {
        try {
            Long memberId = extractMemberId(authorizationHeader);
            memberService.updateNickname(memberId, newname);
            return ResponseEntity.ok(newname);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

}
