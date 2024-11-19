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
            "AVG(r.actorSkill) as avgActorSkill, " +
            "AVG(r.directorSkill) as avgDirectorSkill, " +
            "AVG(r.lineSkill) as avgLineSkill, " +
            "AVG(r.musicSkill) as avgMusicSkill, " +
            "AVG(r.sceneSkill) as avgSceneSkill, " +
            "AVG(r.storySkill) as avgStorySkill) " +
            "FROM ReviewEntity r " +
            "WHERE r.movie.id = :id " +
            "AND ((r.createdDate BETWEEN :startDate AND :endDate) " +
            "OR (r.modifiedDate BETWEEN :startDate AND :endDate))")
    Map<String, Object> findAverageSkillsByMovieIdWithinDateRange(
            @Param("id") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    //ReviewEntity findTopByMemberIdAndMovieIdOrderByCreatedDateDesc(Long memberId, Long movieId);
    //Optional<ReviewEntity> findTopByMemberAndMovieOrderByCreatedDateDesc(MemberEntity member, MovieDetailEntity movie);

    @Query("SELECT new map(" +
            "r.actorSkill as actorSkill, " +
            "r.directorSkill as directorSkill, " +
            "r.lineSkill as lineSkill, " +
            "r.musicSkill as musicSkill, " +
            "r.sceneSkill as sceneSkill, " +
            "r.storySkill as storySkill) " +
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
            "r.movie.id as movieId, " +
            "AVG(r.actorSkill) as avgActorSkill) " +
            "FROM ReviewEntity r " +
            "GROUP BY r.movie.id " +
            "ORDER BY AVG(r.actorSkill) DESC")
    List<Map<String, Object>> findTop20MoviesByAvgActorSkill(Pageable pageable);

    @Query("SELECT new map(" +
            "r.movie.id as movieId, " +
            "AVG(r.directorSkill) as avgDirectorSkill) " +
            "FROM ReviewEntity r " +
            "GROUP BY r.movie.id " +
            "ORDER BY AVG(r.directorSkill) DESC")
    List<Map<String, Object>> findTop20MoviesByAvgDirectorSkill(Pageable pageable);

    @Query("SELECT new map(" +
            "r.movie.id as movieId, " +
            "AVG(r.lineSkill) as avgLineSkill) " +
            "FROM ReviewEntity r " +
            "GROUP BY r.movie.id " +
            "ORDER BY AVG(r.lineSkill) DESC")
    List<Map<String, Object>> findTop20MoviesByAvgLineSkill(Pageable pageable);

    @Query("SELECT new map(" +
            "r.movie.id as movieId, " +
            "AVG(r.musicSkill) as avgMusicSkill) " +
            "FROM ReviewEntity r " +
            "GROUP BY r.movie.id " +
            "ORDER BY AVG(r.musicSkill) DESC")
    List<Map<String, Object>> findTop20MoviesByAvgMusicSkill(Pageable pageable);

    @Query("SELECT new map(" +
            "r.movie.id as movieId, " +
            "AVG(r.sceneSkill) as avgSceneSkill) " +
            "FROM ReviewEntity r " +
            "GROUP BY r.movie.id " +
            "ORDER BY AVG(r.sceneSkill) DESC")
    List<Map<String, Object>> findTop20MoviesByAvgSceneSkill(Pageable pageable);

    @Query("SELECT new map(" +
            "r.movie.id as movieId, " +
            "AVG(r.storySkill) as avgStorySkill) " +
            "FROM ReviewEntity r " +
            "GROUP BY r.movie.id " +
            "ORDER BY AVG(r.storySkill) DESC")
    List<Map<String, Object>> findTop20MoviesByAvgStorySkill(Pageable pageable);

}

