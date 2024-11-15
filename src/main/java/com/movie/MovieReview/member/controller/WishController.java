package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.member.entity.WishEntity;
import com.movie.MovieReview.member.service.WishService;
import com.movie.MovieReview.member.service.WishServiceImpl;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wish")
public class WishController {

    @Autowired
    private WishService wishService;

    /**
     * 위시리스트에 영화를 추가하거나 삭제하는 API
     * @param memberId 회원 ID
     * @param movieId 영화 ID
     * @return ResponseEntity
     */
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleWishlist(@RequestParam Long memberId, @RequestParam Long movieId) {
        try {
            // 위시리스트에 추가 또는 삭제 처리
            wishService.addToWishlist(memberId, movieId);
            return ResponseEntity.ok("위시리스트가 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("오류가 발생했습니다: " + e.getMessage());
        }
    }
    /**
     * 특정 회원의 위시리스트 조회
     * @param memberId 회원 ID
     * @return 회원의 위시리스트 항목 리스트
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MovieCardDto>> getWishlistByMemberId(@PathVariable("memberId") Long memberId) {
        List<MovieCardDto> wishlist = wishService.getWishlistByMemberId(memberId);
        return ResponseEntity.ok(wishlist);
    }


}
