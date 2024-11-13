package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieCreditsDto;
import com.movie.MovieReview.movie.entity.MovieCreditsEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.stereotype.Service;

@Service
public interface MovieCreditService {
    public void saveMovieCredit(MovieCreditsDto movieCreditsDto);

    default MovieCreditsDto toDto(MovieCreditsEntity movieCreditsEntity){
        return MovieCreditsDto.builder()
                .creditId(movieCreditsEntity.getCreditId())
                .movieId(movieCreditsEntity.getMovieDetailEntity().getId())
                .name(movieCreditsEntity.getName())
                .type(movieCreditsEntity.getType())
                .profile(movieCreditsEntity.getProfile())
                .build();
    }

    default MovieCreditsEntity toEntity(MovieCreditsDto movieCreditsDto, MovieDetailEntity movieDetailEntity){
        return MovieCreditsEntity.builder()
                .creditId(movieCreditsDto.getCreditId())
                .movieDetailEntity(movieDetailEntity)
                .name(movieCreditsDto.getName())
                .type(movieCreditsDto.getType())
                .profile(movieCreditsDto.getProfile())
                .build();
    }
}
