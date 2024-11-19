package com.movie.MovieReview.review.repository;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
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
import java.util.Optional;

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

    @Query("SELECT new map(" +
            "AVG(r.actorSkill) as avgActorSkillWithAwards, " +
            "AVG(r.directorSkill) as avgDirectorSkillWithAwards, " +
            "AVG(r.lineSkill) as avgLineSkillWithAwards, " +
            "AVG(r.musicSkill) as avgMusicSkillWithAwards, " +
            "AVG(r.sceneSkill) as avgSceneSkillWithAwards, " +
            "AVG(r.storySkill) as avgStorySkillWithAwards) " +
            "FROM ReviewEntity r " +
            "WHERE r.movie.id = :id " +
            "AND ((r.createdDate BETWEEN :startDate AND :endDate) " +
            "AND (r.modifiedDate BETWEEN :startDate AND :endDate))")
    Map<String, Object> findAverageSkillsByMovieIdWithinDateRange(
            @Param("id") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new map(" +
            "r.actorSkill as myActorSkill, " +
            "r.directorSkill as myDirectorSkill, " +
            "r.lineSkill as myLineSkill, " +
            "r.musicSkill as myMusicSkill, " +
            "r.sceneSkill as mySceneSkill, " +
            "r.storySkill as myStorySkill) " +
            "FROM ReviewEntity r " +
            "WHERE r.member.id = :memberId AND r.movie.id = :movieId " +
            "ORDER BY r.createdDate DESC")
    List<Map<String, Object>> findTopByMemberIdAndMovieIdOrderByCreatedDateDesc(
            @Param("memberId") Long memberId,
            @Param("movieId") Long movieId,
            Pageable pageable);

    @Query("SELECT r FROM ReviewEntity r WHERE r.member.id = :memberId AND r.movie.id = :movieId")
    List<ReviewEntity> findAllReviewsByMemberIdAndMovieId(
            @Param("memberId") Long memberId,
            @Param("movieId") Long movieId);

    @Query("SELECT new map(" +
            "r.actorSkill as myActorSkillWithMyAwards, " +
            "r.directorSkill as myDirectorSkillWithMyAwards, " +
            "r.lineSkill as myLineSkillWithMyAwards, " +
            "r.musicSkill as myMusicSkillWithMyAwards, " +
            "r.sceneSkill as mySceneSkillWithMyAwards, " +
            "r.storySkill as myStorySkillWithMyAwards) " +
            "FROM ReviewEntity r " +
            "WHERE r.member.id = :memberId " +
            "AND r.movie.id = :movieId " +
            "AND r.createdDate BETWEEN :startDate AND :endDate " +
            "AND r.modifiedDate BETWEEN :startDate AND :endDate")
    Optional<Map<String, Object>> findReviewByMemberIdAndMovieIdAndCreatedDateAndModifiedDateBetween(
            @Param("memberId") Long memberId,
            @Param("movieId") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);



}

