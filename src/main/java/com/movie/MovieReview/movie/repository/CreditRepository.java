package com.movie.MovieReview.movie.repository;

import com.movie.MovieReview.movie.entity.MovieCreditsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<MovieCreditsEntity, Long> {
}
