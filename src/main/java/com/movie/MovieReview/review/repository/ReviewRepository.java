package com.movie.MovieReview.review.repository;

import com.movie.MovieReview.review.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT new map(r.member.id as memberId, AVG(r.actorSkill) as avgActorSkill, AVG(r.directorSkill) as avgDirectorSkill, AVG(r.lineSkill) as avgLineSkill, AVG(r.musicSkill) as avgMusicSkill, AVG(r.sceneSkill) as avgSceneSkill, AVG(r.storySkill) as avgStorySkill) FROM ReviewEntity r WHERE r.member.id = :memberId")
    Map<String, Object> findAverageSkillsByMemberId(@Param("memberId") Long memberId);
}
