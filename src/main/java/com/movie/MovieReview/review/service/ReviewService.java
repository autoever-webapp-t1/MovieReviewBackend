package com.movie.MovieReview.review.service;

import com.movie.MovieReview.review.dto.ReviewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    public Long createReview(ReviewDto dto);
    public void modifyReview(ReviewDto dto);
    public void removeReview(Long movieId, Long reviewId);
    public ReviewDto getReview(Long reviewId);
    public List<ReviewDto> getAllReviews();

    void toggleLike(Long reviewId); // 좋아요 토글
}

