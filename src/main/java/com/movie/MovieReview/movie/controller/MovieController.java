package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.movie.dto.MovieListResponse;
import com.movie.MovieReview.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
@CrossOrigin("*")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/top_rated")
    public List<MovieListResponse> getPopularMovies() {
        try {
            return movieService.getPopularMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
