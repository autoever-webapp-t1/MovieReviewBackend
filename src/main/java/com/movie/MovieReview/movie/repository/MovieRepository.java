package com.movie.MovieReview.movie.repository;

import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieDetailEntity,Long> {
    Optional<MovieDetailEntity> findByTitle(String title);

    List<MovieDetailEntity> findByTitleContaining(String query);

    Page<MovieDetailEntity> findByTitleContaining(String title, Pageable pageable);

}
