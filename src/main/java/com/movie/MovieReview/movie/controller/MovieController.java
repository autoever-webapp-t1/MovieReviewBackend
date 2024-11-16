package com.movie.MovieReview.movie.controller;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.service.MovieRecommendService;
import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ReviewService reviewService;

    @GetMapping("/topRated/{memberId}") //topRated가져오기
    public ResponseEntity<?> getTopRatedMovies(@PathVariable("memberId") Long memberId) {
        try {
            List<MovieCardDto> result = movieService.getTopRatedMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/nowPlaying/{memberId}") //nowPlaying가져오기
    public ResponseEntity<?> getNowPlayingMovies(@PathVariable("memberId") Long memberId){
        try {
            List<MovieCardDto> result =movieService.getNowPlayingMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/upComing/{memberId}") //upComing가져오기
    public ResponseEntity<?> getUpComingMovies(@PathVariable("memberId") Long memberId){
        try {
            List<MovieCardDto> result = movieService.getUpComingMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/popular/{memberId}") //popular가져오기
    public ResponseEntity<?> getPopularMovies(@PathVariable("memberId") Long memberId){
        try {
            List<MovieCardDto> result = movieService.getPopularMovies(memberId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
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

//    @GetMapping("/{id}") //영화 상세정보 tmdb에서 가져옴
//    public MovieDetailsDto getMovieDetails(@PathVariable ("id") Long id) {
//        try{
//            log.info("MovieController: 영화아이디 값은?" + id);
//            return movieService.getMovieDetails(id);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @GetMapping("/topRatedDetails") //DB에 저장된 id바탕으로 상세정보 저장
    public List<MovieDetailsDto> getTopRatedMovieDetails() {
        try {
            return movieService.getTopRatedMovieDetails();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @GetMapping("/{id}/{memberId}") //우리 DB에서 영화 상세정보 검색
    public MovieDetailsDto getTopRatedMovieDetailsInDB(@PathVariable ("id") Long id, @PathVariable ("memberId") Long memberId) {
        try{
            return movieService.getTopRatedMovieDetailsInDB(id, memberId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @GetMapping("/search/{name}") //영화 제목으로 검색
//    public MovieDetailsDto searchMovie(@PathVariable ("name") String name){
//        try{
//            return movieService.searchMovie(name);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @GetMapping("/search")
//    public List<MovieCardDto> search(@RequestParam String query) {
//        return movieService.searchByQuery(query); // 이름에 query가 포함된 제품을 검색
//    }

    @GetMapping("/search/{keyword}") //키워드 포함되어 있는 거 모두 검색 with 페이지네이션
    public ResponseEntity<PageResponseDto<MovieCardDto>> getKeywordResult(
            @PathVariable("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page)
                .size(size)
                .build();
        PageResponseDto<MovieCardDto> response = movieService.getAllMovieByKeyword(keyword, pageRequestDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommendations/{memberId}")
    public ResponseEntity<List<MovieCardDto>> getMemberRecommendByMemberId(@PathVariable("memberId") Long memberId) {
        log.info("Request received for memberId: {}", memberId);  // memberId 확인 로그
        List<MovieCardDto> movieCards = movieService.getMovieMemberRecommendations(memberId);
        return ResponseEntity.ok(movieCards);
    }
}

