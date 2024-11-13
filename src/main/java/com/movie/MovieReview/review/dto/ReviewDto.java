package com.movie.MovieReview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewId;
    private Long movieId;
    private Long memberId;
    private String content;
    private LocalDateTime createdDate;
    private int totalHeart;
    private boolean myHeart;

    private int actorSkill;
    private int directorSkill;
    private int sceneSkill;
    private int musicSkill;
    private int storySkill;
    private int lineSkill;

    private double avgSkill;
}
