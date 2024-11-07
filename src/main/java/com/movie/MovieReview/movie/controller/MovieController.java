package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.movie.dto.MovieListResponse;
import com.movie.MovieReview.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/popular")
    public MovieListResponse getPopularMovies() {
        try {
            return movieService.getPopularMovies();
        } catch (Exception e) {
            return null;
        }
    }
}

