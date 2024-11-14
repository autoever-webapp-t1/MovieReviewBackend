package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieCreditsDto;
import com.movie.MovieReview.movie.entity.MovieCreditsEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.repository.CreditRepository;
import com.movie.MovieReview.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MovieCreditServiceImpl implements MovieCreditService {
    private final MovieRepository movieRepository;
    private final CreditRepository creditRepository;

    @Override
    public void saveMovieCredit(MovieCreditsDto movieCreditsDto) {
        Long movieId = movieCreditsDto.getMovieId();

        Optional<MovieDetailEntity> movie = movieRepository.findById(movieId);
        MovieDetailEntity movieDetailEntity = movie.orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        MovieCreditsEntity movieCreditsEntity = toEntity(movieCreditsDto, movieDetailEntity);
        creditRepository.save(movieCreditsEntity);
    }
}
