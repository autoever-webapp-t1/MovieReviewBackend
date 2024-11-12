package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.service.MovieServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/movie")
@CrossOrigin("*")
@Log4j2
public class MovieController {

    private final MovieServiceImpl movieService;

    @GetMapping("/topRated")
    public List<MovieCardDto> getTopRatedMovies() {
        try {
            return movieService.getTopRatedMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/nowPlaying")
    public List<MovieCardDto> getNowPlayingMovies(){
        try {
            return movieService.getNowPlayingMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/{id}")
    public MovieDetailsDto getMovieDetails(@PathVariable ("id") Long id) {
        try{
            log.info("MovieController: 영화아이디 값은?" + id);
            return movieService.getMovieDetails(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    @PostMapping("/SaveTopRated")
//    public Long SaveTopRated(@RequestBody MovieCardDto movieCardDto){
//        try{
//            Long movieId = movieService.SaveTopRated(movieCardDto);
//            return movieId;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


}
