package com.movie.MovieReview.movie.repository;

import com.movie.MovieReview.movie.entity.TopRatedMovieIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopRatedMovieIdRepository extends JpaRepository<TopRatedMovieIdEntity,Long> {
}
