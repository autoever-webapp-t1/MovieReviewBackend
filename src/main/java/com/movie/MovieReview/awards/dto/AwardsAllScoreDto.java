package com.movie.MovieReview.awards.dto;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwardsAllScoreDto {

    //1등 영화 영화 하나에 대한 DTO
    private Long movieId; //nominatedId (=movieId)

    //이건 AwardsMovieCardDto와 동일하게 하면 될듯
    private Map<String, Object> awardsScore; // 어워즈 기간 내 평균 스킬 데이터 + totalAvgSkill을 저장할 awardsScore 필드

    private Map<String, Object> awardsMyScore; //어워즈 기간 내 나의 평균 스킬 데이터 + avgSkill을 저장할 awardsMyScore 필드
}
