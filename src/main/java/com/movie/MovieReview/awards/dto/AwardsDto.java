package com.movie.MovieReview.awards.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AwardsDto {
    private Long awardsId;

    private String awardName;

    private Long nominated1;

    private Long nominated2;

    private Long nominated3;

    private Long nominated4;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

}
