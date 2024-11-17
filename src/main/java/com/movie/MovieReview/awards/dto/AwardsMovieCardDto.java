package com.movie.MovieReview.awards.dto;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwardsMovieCardDto {
    private Long movieId; //nominatedId (=movieId)

    private String movieTitle; //nominated Movie title

    private Map<String, Object> score; // 평균 스킬 데이터 + totalAvgSkill을 저장할 score 필드
}
