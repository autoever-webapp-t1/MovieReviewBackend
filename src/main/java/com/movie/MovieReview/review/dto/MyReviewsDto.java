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
public class MyReviewsDto {
    private Long reviewId;

    private Long movieId;
    private String title;
    private String images;

    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifyDate;

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
