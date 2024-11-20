package com.movie.MovieReview.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDetailDto {
    private Long reviewId;
    private Long movieId;
    private Long memberId;

    private String title;

    private String nickname;
    private String profile;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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

    private String poster_path;
}
