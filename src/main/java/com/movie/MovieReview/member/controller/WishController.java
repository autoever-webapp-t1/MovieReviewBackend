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

//  위시리스트에 영화를 추가하거나 삭제하는 API (toggle형태)
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
//    회원의 wishlist 조회
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MovieCardDto>> getWishlistByMemberId(@PathVariable("memberId") Long memberId) {
        List<MovieCardDto> wishlist = wishService.getWishlistByMemberId(memberId);
        return ResponseEntity.ok(wishlist);
    }


}
