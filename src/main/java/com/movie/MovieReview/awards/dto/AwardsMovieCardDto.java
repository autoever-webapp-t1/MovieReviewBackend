package com.movie.MovieReview.awards.dto;

import java.util.Map;

public class AwardsMovieCardDto {
    private Long movieId;
    private String movieTitle;

    //private Double awardsTotalAverageSkill;

    private Map<String, Object> score; // 평균 스킬 데이터 + totalAvgSkill을 저장할 score 필드
}
