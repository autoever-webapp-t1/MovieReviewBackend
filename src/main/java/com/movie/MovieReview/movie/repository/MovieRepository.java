package com.movie.MovieReview.movie.repository;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieDetailEntity,Long> {
    Optional<MovieDetailEntity> findByTitle(String title);
}
