package com.movie.MovieReview.review.controller;

import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.MovieReview.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    //리뷰 생성
    @PostMapping("/movie/{movieId}/review")
    public ResponseEntity<?> createReview(
            @PathVariable("movieId") Long movieId,
            @RequestHeader("Authorization") String authorizationHeader,
            @Validated @RequestBody ReviewDetailDto dto) {
        try {
            Long memberId = extractMemberId(authorizationHeader);
            if (movieId == null || memberId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie ID and Member ID must not be null.");
            }

            dto.setMovieId(movieId);
            dto.setMemberId(memberId);

            log.info("memberId: " + dto.getMemberId());

            Long reviewId = reviewService.createReview(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body("Review created with ID: " + reviewId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        }
    }

    // 리뷰 수정
    @PutMapping("/movie/{id}/review/{reviewId}")
    public ResponseEntity<?> modifyReview(
            @PathVariable("id") Long id,
            @PathVariable("reviewId") Long reviewId,
            @RequestHeader("Authorization") String authorizationHeader,
            @Validated @RequestBody ReviewDetailDto dto) {
        try {
            Long memberId = extractMemberId(authorizationHeader);

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

    //리뷰 하나를 가지고 올 때 reviewId로 하는게 아닌, movieId로 수정
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
    @GetMapping("/user/rate")
    public ResponseEntity<?> getAverageSkillsByMemberId(@RequestHeader("Authorization") String authorizationHeader){
        try{
            Long memberId = extractMemberId(authorizationHeader);
            Map<String, Object> averageSkills = reviewService.getAverageSkillsByMemberId(memberId);
            return ResponseEntity.ok(averageSkills);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반환 값 오류!!!!");
        }
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

    //awards 기간 마다에 movie 당 평균값 내기 (여섯개 skill의 avg + totalAvg)
    @GetMapping("/movie/{movieId}/awards-rate")
    public ResponseEntity<Map<String, Object>> getAverageSkillsByMovieIdDateRange(
            @PathVariable("movieId") Long movieId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")  LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")  LocalDateTime endDate) {
        Map<String, Object> totalAvgSkills = reviewService.getAverageSkillsByMovieIdAndDateRange(movieId,startDate,endDate);

        if (totalAvgSkills.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No reviews found for this movie"));
        }

        return ResponseEntity.ok(totalAvgSkills); // totalAverageSkill 포함된 avgSkills 반환
    }

    //mypage에서 리뷰 리스트 보여주는거
    @GetMapping("/user/reviews")
    public ResponseEntity<PageResponseDto<ReviewDetailDto>> getAllReviewsByMemberId(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page)
                .size(size)
                .build();

        Long memberId = extractMemberId(authorizationHeader);
        PageResponseDto<ReviewDetailDto> response = reviewService.getAllReviewsByMemberId(memberId, pageRequestDto);

        return ResponseEntity.ok(response);
    }

    //main용
    //memberId로 movieId를 찾아서 List<MovieCardsDto>로 반환
    @GetMapping("/member/movie-cards")
    public ResponseEntity<List<MovieCardDto>> getMovieCardDtosByMemberId(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        Long memberId = extractMemberId(authorizationHeader);
        List<MovieCardDto> movieCards = reviewService.getMovieCardDtosByMemberId(memberId);
        return ResponseEntity.ok(movieCards);
    }
}