package com.movie.MovieReview.awards.repository;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwardsRepository extends JpaRepository<AwardsEntity,Long> {
    List<AwardsEntity> findByStatus(int status);

//    @Query("SELECT a FROM AwardsEntity a WHERE a.topMovieId = :topMovieId")
    List<AwardsEntity> findByTopMovieId(@Param("topMovieId") Long topMovieId);
}
