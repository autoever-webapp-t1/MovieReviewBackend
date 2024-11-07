package com.movie.MovieReview.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieListResponse {
    private int page;
    private List<MovieDto> results;
}
