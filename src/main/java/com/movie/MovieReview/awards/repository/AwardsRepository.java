package com.movie.MovieReview.awards.repository;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardsRepository extends JpaRepository<AwardsEntity,Long> {
    List<AwardsEntity> findByStatus(int status);
}
