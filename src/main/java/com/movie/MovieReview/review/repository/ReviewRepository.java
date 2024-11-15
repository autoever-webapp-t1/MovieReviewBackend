package com.movie.MovieReview.review.repository;

import com.movie.MovieReview.review.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT new map(r.member.id as memberId, AVG(r.actorSkill) as avgActorSkill, AVG(r.directorSkill) as avgDirectorSkill, AVG(r.lineSkill) as avgLineSkill, AVG(r.musicSkill) as avgMusicSkill, AVG(r.sceneSkill) as avgSceneSkill, AVG(r.storySkill) as avgStorySkill) FROM ReviewEntity r WHERE r.member.id = :memberId")
    Map<String, Object> findAverageSkillsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.member.id = :memberId")
    List<ReviewEntity> findAllReviewsByMemberId(@Param("memberId") Long memberId);

    Page<ReviewEntity> findByMovieId(Long movieId, Pageable pageable);

    @Query("SELECT r FROM ReviewEntity r WHERE r.member.id = :memberId")
    Page<ReviewEntity> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT new map(AVG(r.actorSkill) as avgActorSkill, AVG(r.directorSkill) as avgDirectorSkill, " +
            "AVG(r.lineSkill) as avgLineSkill, AVG(r.musicSkill) as avgMusicSkill, " +
            "AVG(r.sceneSkill) as avgSceneSkill, AVG(r.storySkill) as avgStorySkill) " +
            "FROM ReviewEntity r WHERE r.movie.id = :movieId")
    Map<String, Object> findAverageSkillsByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT new map(AVG(r.actorSkill) as avgActorSkill, AVG(r.directorSkill) as avgDirectorSkill, " +
            "AVG(r.lineSkill) as avgLineSkill, AVG(r.musicSkill) as avgMusicSkill, " +
            "AVG(r.sceneSkill) as avgSceneSkill, AVG(r.storySkill) as avgStorySkill) " +
            "FROM ReviewEntity r " +
            "WHERE r.movie.id = :movieId " +
            "AND r.createdDate BETWEEN :startDate AND :endDate")
    Map<String, Object> findAverageSkillsByMovieIdWithinDateRange(
            @Param("movieId") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}

