package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.dto.TopRatedResponse;

import java.util.List;

public interface MovieService {
    public List<TopRatedResponse> getTopRatedMovies() throws Exception; //TopRated 100개 영화정보 가져오기
    public MovieDetailsDto getMovieDetails(Long id) throws Exception; //영화상세정보 가져오기
}
