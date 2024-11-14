package com.movie.MovieReview.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRecommendDto {
    private Long recommendationsMovieId;
    private Long movieId;
    private Long recommendationMovieId;
}
