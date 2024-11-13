package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {
    public List<MovieCardDto> getTopRatedMovies() throws Exception; //TopRated 100개 영화정보 가져오기
    public List<MovieCardDto> getNowPlayingMovies() throws Exception;
    public List<MovieCardDto> getUpComingMovies() throws Exception;
    public MovieDetailsDto getMovieDetails(Long id) throws Exception; //영화상세정보 가져오기
    //public void SaveMovieDetail(MovieDetailsDto movieDetailsDto);
    public List<Long> SaveTopRatedId() throws Exception;
    public List<MovieDetailsDto> getTopRatedMovieDetails() throws Exception;

    default MovieDetailsDto toDto(MovieDetailEntity movieDetailEntity){
        return MovieDetailsDto.builder()
                .id(movieDetailEntity.getId())
                .title(movieDetailEntity.getTitle())
                .overview(movieDetailEntity.getOverview())
                .release_date(movieDetailEntity.getRelease_date())
                .runtime(movieDetailEntity.getRuntime())
                .images(movieDetailEntity.getImages())
                .videos(movieDetailEntity.getVideos())
                .build();
    }

    default MovieDetailEntity toEntity(MovieDetailsDto movieDetailsDto){
        return MovieDetailEntity.builder()
                .id(movieDetailsDto.getId())
                .title(movieDetailsDto.getTitle())
                .overview(movieDetailsDto.getOverview())
                .release_date(movieDetailsDto.getRelease_date())
                .runtime(movieDetailsDto.getRuntime())
                .images(movieDetailsDto.getImages())
                .videos(movieDetailsDto.getVideos())
                .build();
    }


}
