package com.movie.MovieReview.movie.repository;

import com.movie.MovieReview.movie.entity.MovieRecommendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<MovieRecommendEntity,Long> {
}
