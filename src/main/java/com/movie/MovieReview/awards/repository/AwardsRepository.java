package com.movie.MovieReview.awards.repository;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardsRepository extends JpaRepository<AwardsEntity,Long> {
}
