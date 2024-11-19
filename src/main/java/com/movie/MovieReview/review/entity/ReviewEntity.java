package com.movie.MovieReview.review.entity;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review")
public class ReviewEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne
    @JoinColumn(name="id")
    private MovieDetailEntity movie;

    @ManyToOne
    @JoinColumn(name="member_id")
    private MemberEntity member;

    private String content;

    @Builder.Default
    @Column(nullable = false)
    private int totalHeart = 0; //총 좋아요 수

    @Builder.Default
    @Column(nullable = false)
    private boolean myHeart = false; //나의 좋아요 클릭 여부

    @Column(nullable = false)
    private int actorSkill;

    @Column(nullable = false)
    private int directorSkill;

    @Column(nullable = false)
    private int sceneSkill;

    @Column(nullable = false)
    private int musicSkill;

    @Column(nullable = false)
    private int storySkill;

    @Column(nullable = false)
    private int lineSkill;

    // 좋아요 상태 토글 메서드
    public void toggleLikeHeart() {
        if (this.myHeart) {
            this.myHeart = false;
            this.totalHeart = Math.max(0, this.totalHeart - 1); // 0 이하로 내려가지 않도록 처리
        } else {
            this.myHeart = true;
            this.totalHeart += 1;
        }
    }

    //review update
    public void updateReview(ReviewDetailDto dto) {
        this.content = dto.getContent();
        this.totalHeart = dto.getTotalHeart();
        this.myHeart = dto.isMyHeart();
        this.actorSkill = dto.getActorSkill();
        this.directorSkill = dto.getDirectorSkill();
        this.sceneSkill = dto.getSceneSkill();
        this.musicSkill = dto.getMusicSkill();
        this.storySkill = dto.getStorySkill();
        this.lineSkill = dto.getLineSkill();
    }
}
