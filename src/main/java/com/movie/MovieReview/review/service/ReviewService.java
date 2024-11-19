package com.movie.MovieReview.review.service;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.review.dto.MyReviewsDto;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.MovieReview.review.entity.ReviewEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    public Long createReview(ReviewDetailDto dto); //리뷰 작성
    public void modifyReview(ReviewDetailDto dto); //리뷰 수정
    public void removeReview(Long movieId, Long reviewId); //리뷰 삭제
    public ReviewDetailDto getReview(Long reviewId); //리뷰 하나 detail
    public List<ReviewDetailDto> getAllReviews(); //모든 리뷰

    public PageResponseDto<ReviewDetailDto> getAllReviewsByMovieId(Long movieId, PageRequestDto pageRequestDto); //movie ID로 리뷰 목록 페이징 조회
    public PageResponseDto<ReviewDetailDto> getAllReviewsByMemberId(Long memberId, PageRequestDto pageRequestDto); //member ID로 리뷰 목록 페이징 조회

    public Map<String, Object> getAverageSkillsByMemberId(Long memberId); //회원 리뷰의 평균 통계
    public Map<String, Object> getAverageSkillsByMovieId(Long movieId); //영화 리뷰의 평균 통계
    public List<MyReviewsDto> getMemberReviews(Long memberId);
    public Map<String, Object> getLatestReviewSkills(Long memberId, Long movieId); //가장 최근에 작성한 리뷰의 6개 skill + avgSkill
    public List<MovieCardDto> getMovieCardDtosByMemberId(Long memberId); //회원이 본 영화를 CardDto로 제공

    //for awards
    public Map<String, Object> getAverageSkillsByMovieIdAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate); //어워즈 기간동안 영화 하나의 모든 리뷰의 skill 값 통계 + totalAvgSkill
    public Map<String, Object> getAwardsReviewSkills(Long memberId, Long movieId, LocalDateTime awardStartDate, LocalDateTime awardEndDate);
    ReviewDetailDto toDto(ReviewEntity reviewEntity);
}


