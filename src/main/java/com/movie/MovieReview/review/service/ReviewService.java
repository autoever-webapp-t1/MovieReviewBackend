package com.movie.MovieReview.review.service;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.review.dto.MyReviewsDto;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    public Long createReview(ReviewDetailDto dto);
    public void modifyReview(ReviewDetailDto dto);
    public void removeReview(Long movieId, Long reviewId);
    public ReviewDetailDto getReview(Long reviewId);
    public List<ReviewDetailDto> getAllReviews();
    public PageResponseDto<ReviewDetailDto> getAllReviewsByMovieId(Long movieId, PageRequestDto pageRequestDto);

    public PageResponseDto<ReviewDetailDto> getAllReviewsByMemberId(Long memberId, PageRequestDto pageRequestDto);

    void toggleLike(Long reviewId); // 좋아요 토글

    public Map<String, Object> getAverageSkillsByMemberId(Long memberId); //회원의 통계 평균
    public Map<String, Object> getAverageSkillsByMovieId(Long movieId);
    public List<MyReviewsDto> getMemberReviews(Long memberId);
    public Map<String, Object> getLatestReviewSkills(Long memberId, Long movieId);
    public List<MovieCardDto> getMovieCardDtosByMemberId(Long memberId);

    //for awards
    public Map<String, Object> getAverageSkillsByMovieIdAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate);
}


