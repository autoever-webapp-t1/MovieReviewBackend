package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.entity.MovieEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {
    public List<MovieCardDto> getTopRatedMovies() throws Exception; //TopRated 100개 영화정보 가져오기
    public List<MovieCardDto> getNowPlayingMovies() throws Exception;
    public MovieDetailsDto getMovieDetails(Long id) throws Exception; //영화상세정보 가져오기
    public void SaveTopRated(MovieCardDto movieCardDto);

    default MovieCardDto toDto(MovieEntity movieEntity){
        return MovieCardDto.builder()
                .id(movieEntity.getId())
                .title(movieEntity.getTitle())
                .overview(movieEntity.getOverview())
                .poster_path(movieEntity.getPoster_path())
                .build();
    }

    default MovieEntity toEntity(MovieCardDto movieCardDto){
        return MovieEntity.builder()
                .id(movieCardDto.getId())
                .title(movieCardDto.getTitle())
                .overview(movieCardDto.getOverview())
                .poster_path(movieCardDto.getPoster_path())
                .build();
    }
}
