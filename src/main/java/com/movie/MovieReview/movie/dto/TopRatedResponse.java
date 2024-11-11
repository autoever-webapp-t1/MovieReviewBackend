package com.movie.MovieReview.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopRatedResponse {
    private List<TopRatedDto> results;
}
