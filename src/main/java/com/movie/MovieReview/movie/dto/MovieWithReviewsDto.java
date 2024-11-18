package com.movie.MovieReview.movie.dto;

import com.movie.MovieReview.review.dto.ReviewDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class MovieWithReviewsDto {
    private MovieDetailsDto movieDetails; // 영화 상세 정보
    private List<ReviewDetailDto> reviews; // 리뷰 리스트
}

