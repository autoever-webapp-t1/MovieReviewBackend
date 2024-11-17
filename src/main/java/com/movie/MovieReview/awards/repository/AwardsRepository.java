package com.movie.MovieReview.awards.repository;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardsRepository extends JpaRepository<AwardsEntity,Long> {
    List<AwardsEntity> findByStatus(int status);
    AwardsEntity findByTopMovieId(Long movieId);

    List<AwardsEntity> findAllByStatus(int status);
}
