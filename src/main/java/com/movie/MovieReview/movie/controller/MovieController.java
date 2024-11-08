package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.movie.dto.MovieListResponse;
import com.movie.MovieReview.movie.service.MovieService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {

    private MovieService movieService;

    @GetMapping("/top_rated")
    public List<MovieListResponse> getPopularMovies() {
        try {
            return movieService.getPopularMovies();
        } catch (Exception e) {
            return null;
        }
    }
}
