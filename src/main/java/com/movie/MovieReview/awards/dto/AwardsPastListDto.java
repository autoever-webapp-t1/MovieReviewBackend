package com.movie.MovieReview.awards.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AwardsPastListDto {
    private Long awardsId; // 어워즈 ID
    private String awardsName; //어워즈 이fma
    private List<AwardsMovieCardDto> nominatedMovies; // 후보 영화 리스트
}
