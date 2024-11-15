package com.movie.MovieReview.review.controller;

import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.dto.MyReviewsDto;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.MovieReview.review.entity.ReviewEntity;
import com.movie.MovieReview.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin("*")
@Log4j2
public class ReviewController {

    private final ReviewService reviewService;

    private final MovieService movieService;

    //리뷰 생성
    @PostMapping("/movie/{movieId}/review")
    public ResponseEntity<?> createReview(
            @PathVariable("movieId") Long movieId,
            @RequestParam Long memberId,
            @Validated @RequestBody ReviewDetailDto dto) {
        try {
            if (movieId == null || memberId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie ID and Member ID must not be null.");
            }

            dto.setMovieId(movieId);
            dto.setMemberId(memberId);

            log.info("memberId: " + dto.getMemberId());

            Long reviewId = reviewService.createReview(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Review created with ID: " + reviewId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        }
    }

    // 리뷰 수정
    @PutMapping("/movie/{id}/review/{reviewId}")
    public ResponseEntity<?> modifyReview(
            @PathVariable("id") Long id,
            @PathVariable("reviewId") Long reviewId,
            @RequestParam Long memberId,
            @Validated @RequestBody ReviewDetailDto dto) {
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
    @DeleteMapping("/movie/{id}/review/{reviewId}")
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

    // 리뷰 하나 조회
    @GetMapping("/movie/{id}/review/{reviewId}")
    public ResponseEntity<?> getReview(@PathVariable("reviewId") Long reviewId) {
        try {
            ReviewDetailDto reviewDetailDto = reviewService.getReview(reviewId);
            return ResponseEntity.ok(reviewDetailDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }



    // 모든 리뷰 조회
    /*@GetMapping("/movie/{id}/review")
    public ResponseEntity<?> getAllReviews() {
        try {
            return ResponseEntity.ok(reviewService.getAllReviews());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }*/

    //영화 마다 리뷰 pagenation
    @GetMapping("/movie/{id}/review")
    public ResponseEntity<PageResponseDto<ReviewDetailDto>> getAllReviewsByMovieId(
            @PathVariable("id") Long movieId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page)
                .size(size)
                .build();
        PageResponseDto<ReviewDetailDto> response = reviewService.getAllReviewsByMovieId(movieId, pageRequestDto);

        return ResponseEntity.ok(response);
    }

    //member 당 평균값 내기
    @GetMapping("/user/{memberId}/rate")
    public ResponseEntity<Map<String, Object>> getAverageSkillsByMemberId(@PathVariable Long memberId){
        Map<String, Object> averageSkills = reviewService.getAverageSkillsByMemberId(memberId);
        return ResponseEntity.ok(averageSkills);
    }

    //movie 당 평균값 내기 (여섯개 skill의 avg + totalAvg)
    @GetMapping("/movie/{movieId}/rate")
    public ResponseEntity<Map<String, Object>> getAverageSkillsByMovieId(@PathVariable Long movieId) {
        Map<String, Object> totalAvgSkills = reviewService.getAverageSkillsByMovieId(movieId);

        if (totalAvgSkills.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No reviews found for this movie"));
        }

        return ResponseEntity.ok(totalAvgSkills); // totalAverageSkill 포함된 avgSkills 반환
    }


    //member의 모든 리뷰 조회
    /*@GetMapping("/user/{memberId}/reviews")
    public ResponseEntity<List<MyReviewsDto>> getMemberReviews(@PathVariable Long memberId) {
        List<MyReviewsDto> reviews = reviewService.getMemberReviews(memberId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }*/

    @GetMapping("/user/{memberId}/reviews")
    public ResponseEntity<PageResponseDto<ReviewDetailDto>> getAllReviewsByMemberId(
            @PathVariable("memberId") Long memberId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page)
                .size(size)
                .build();

        PageResponseDto<ReviewDetailDto> response = reviewService.getAllReviewsByMemberId(memberId, pageRequestDto);

        return ResponseEntity.ok(response);
    }


}