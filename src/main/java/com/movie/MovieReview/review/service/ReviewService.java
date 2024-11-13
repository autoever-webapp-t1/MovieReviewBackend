package com.movie.MovieReview.review.service;

import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.MovieReview.review.entity.ReviewEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    public Long createReview(ReviewDetailDto dto);
    public void modifyReview(ReviewDetailDto dto);
    public void removeReview(Long movieId, Long reviewId);
    public ReviewDetailDto getReview(Long reviewId);
    public List<ReviewDetailDto> getAllReviews();

    void toggleLike(Long reviewId); // 좋아요 토글

    public Map<String, Object> getAverageSkillsByMemberId(Long memberId); //회원의 통계 평균
    public List<ReviewEntity> getMemberReviews(Long memberId); //
}

