package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.member.entity.WishEntity;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.member.service.WishService;
import com.movie.MovieReview.member.service.WishServiceImpl;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wish")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    private final JwtTokenService jwtTokenService;

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

//  위시리스트에 영화를 추가하거나 삭제하는 API (toggle형태)
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleWishlist(@RequestHeader("Authorization") String authorizationHeader, @RequestParam Long movieId) {
        try {
            Long memberId = extractMemberId(authorizationHeader);
            // 위시리스트에 추가 또는 삭제 처리
            wishService.addToWishlist(memberId, movieId);
            return ResponseEntity.ok("위시리스트가 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("오류가 발생했습니다: " + e.getMessage());
        }
    }
//    회원의 wishlist 조회
    @GetMapping("/member")
    public ResponseEntity<?> getWishlistByMemberId(@RequestHeader("Authorization") String authorizationHeader) {
        try{
            Long memberId = extractMemberId(authorizationHeader);
            List<MovieCardDto> wishlist = wishService.getWishlistByMemberId(memberId);
            return ResponseEntity.ok(wishlist);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("오류가 발생했습니다: " + e.getMessage());
        }
    }


}
