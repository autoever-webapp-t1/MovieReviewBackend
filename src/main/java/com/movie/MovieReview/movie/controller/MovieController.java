package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.service.MovieRecommendService;
import com.movie.MovieReview.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/movie")
@CrossOrigin("*")
@Log4j2
public class MovieController {

    private final MovieService movieService;
    private final MovieRecommendService movieRecommendService;

    @GetMapping("/topRated") //topRated가져오기
    public List<MovieCardDto> getTopRatedMovies() {
        try {
            return movieService.getTopRatedMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/nowPlaying") //nowPlaying가져오기
    public List<MovieCardDto> getNowPlayingMovies(){
        try {
            return movieService.getNowPlayingMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/upComing") //upComing가져오기
    public List<MovieCardDto> getUpComingMovies(){
        try {
            return movieService.getUpComingMovies();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/SaveTopRatedId") //topRated 영화들 id값만 db에 저장
    public List<Long> SaveTopRatedId(){
        try{
            return movieService.SaveTopRatedId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}") //영화 상세정보 tmdb에서 가져옴
    public MovieDetailsDto getMovieDetails(@PathVariable ("id") Long id) {
        try{
            log.info("MovieController: 영화아이디 값은?" + id);
            return movieService.getMovieDetails(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/topRatedDetails") //DB에 저장된 id바탕으로 상세정보 저장
    public List<MovieDetailsDto> getTopRatedMovieDetails() {
        try {
            return movieService.getTopRatedMovieDetails();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/DetailDB/{id}") //우리 DB에서 영화 상세정보 검색
    public MovieDetailsDto getTopRatedMovieDetailsInDB(@PathVariable ("id") Long id) {
        try{
            return movieService.getTopRatedMovieDetailsInDB(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/search/{name}") //영화 제목으로 검색
    public MovieDetailsDto searchMovie(@PathVariable ("name") String name){
        try{
            return movieService.searchMovie(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

