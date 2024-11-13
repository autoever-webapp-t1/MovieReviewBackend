package com.movie.MovieReview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyReviewsDto {
    private Long reviewId;
    private Long movieId;

    private String title;
    private String poster_path;
}
