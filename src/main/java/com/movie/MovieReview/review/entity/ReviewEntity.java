package com.movie.MovieReview.review.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "review")
public class ReviewEntity {
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDate;

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

    @PrePersist // 엔티티가 처음 생성될 때 호출
    public void prePersist() {
        this.createdDate = LocalDateTime.now(); // 현재 시간으로 초기화
        this.modifiedDate = LocalDateTime.now(); // 생성 시 수정 날짜도 초기화
    }

    @PreUpdate // 엔티티가 업데이트될 때 호출
    public void preUpdate() {
        this.modifiedDate = LocalDateTime.now(); // 수정 시간 업데이트
    }


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
