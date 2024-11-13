package com.movie.MovieReview.review.controller;

import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.dto.ReviewDto;
import com.movie.MovieReview.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movie")
@CrossOrigin("*")
public class ReviewController {

    private final ReviewService reviewService;

    private final MovieService movieService;

    //리뷰 생성
    @PostMapping("/{movieId}/review")
    public ResponseEntity<?> createReview(
            @PathVariable("movieId") Long movieId,
            @RequestParam Long memberId,
            @Validated @RequestBody ReviewDto dto) {
        try {
            if (movieId == null || memberId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie ID and Member ID must not be null.");
            }

            dto.setMovieId(movieId);
            dto.setMemberId(memberId);

            Long reviewId = reviewService.createReview(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Review created with ID: " + reviewId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("뭐지");
        }
    }

    // 리뷰 수정
    @PutMapping("/{id}/review/{reviewId}")
    public ResponseEntity<?> modifyReview(
            @PathVariable("id") Long id,
            @PathVariable("reviewId") Long reviewId,
            @RequestParam Long memberId,
            @Validated @RequestBody ReviewDto dto) {
        try {
            dto.setMovieId(id);
            dto.setReviewId(reviewId);
            dto.setMemberId(memberId);
            reviewService.modifyReview(dto);
            return ResponseEntity.ok("Review updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}/review/{reviewId}")
    public ResponseEntity<?> removeReview(
            @PathVariable("id") Long id,
            @PathVariable("reviewId") Long reviewId) {
        try {
            reviewService.removeReview(id, reviewId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // 리뷰 조회
    @GetMapping("/{id}/review/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable("reviewId") Long reviewId) {
        try {
            ReviewDto reviewDto = reviewService.getReview(reviewId);
            return ResponseEntity.ok(reviewDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    // 모든 리뷰 조회
    @GetMapping("/{id}/review")
    public ResponseEntity<?> getAllReviews() {
        try {
            return ResponseEntity.ok(reviewService.getAllReviews());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
