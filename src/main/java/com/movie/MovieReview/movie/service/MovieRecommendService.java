package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieRecommendDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.entity.MovieRecommendEntity;

public interface MovieRecommendService {

    default MovieRecommendDto toDto(MovieRecommendEntity movieRecommendEntity){
        return MovieRecommendDto.builder()
                .recommendationsMovieId(movieRecommendEntity.getRecommendationsId())
                .movieId(movieRecommendEntity.getMovieDetailEntity().getId())
                .recommendationMovieId(movieRecommendEntity.getRecommendationsId())
                .build();
    }

    default MovieRecommendEntity toEntity(MovieRecommendDto movieRecommendDto, MovieDetailEntity movieDetailEntity){
        return MovieRecommendEntity.builder()
                .recommendationsId(movieRecommendDto.getRecommendationsMovieId())
                .movieDetailEntity(movieDetailEntity)
                .recommendationId(movieRecommendDto.getRecommendationMovieId())
                .build();
    }

    public void saveRecommendations(MovieRecommendDto movieRecommendDto);
}
