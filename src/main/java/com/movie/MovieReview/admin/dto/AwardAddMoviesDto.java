package com.movie.MovieReview.admin.dto;

import lombok.Data;

@Data
public class AwardAddMoviesDto {
    private String awardName;
    private Long[] movieIds = new Long[4];
}
