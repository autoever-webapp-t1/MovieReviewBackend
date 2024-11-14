package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieRecommendDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.entity.MovieRecommendEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.movie.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MovieRecommendServiceImpl implements MovieRecommendService{
    private final MovieRepository movieRepository;
    private final RecommendRepository recommendRepository;

    @Override
    public void saveRecommendations(MovieRecommendDto movieRecommendDto) {
        Long movieId = movieRecommendDto.getMovieId();
        Optional<MovieDetailEntity> movie = movieRepository.findById(movieId);
        MovieDetailEntity movieDetailEntity = movie.orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        MovieRecommendEntity movieRecommendEntity = toEntity(movieRecommendDto, movieDetailEntity);
        recommendRepository.save(movieRecommendEntity);
    }
}
