package com.movie.MovieReview.movie.service;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public interface MovieService {
    public List<MovieCardDto> getTopRatedMovies(Long memberId) throws Exception; //TopRated 100개 영화정보 가져오기
    public List<MovieCardDto> getNowPlayingMovies(Long memberId) throws Exception; //NowPlaying 100개 영화정보 가져오기
    public List<MovieCardDto> getUpComingMovies(Long memberId) throws Exception; //UpComing 100개 영화정보 가져오기
    public List<MovieCardDto> getPopularMovies(Long memberId) throws Exception;
    public List<MovieCardDto> getRecommendMovies(Long movieId, Long memberId) throws Exception; //무비 아이디 가지고 recommendations api호출하기

    public MovieDetailsDto getMovieDetails(Long id) throws Exception; //영화상세정보 가져오기
    public List<Long> SaveTopRatedId() throws Exception; //TopRated ID들 저장

    public List<Long> SavePopularId() throws Exception;

    public List<Long> SaveNowPlayingId() throws Exception;

    List<Long> SaveUpComingId() throws Exception;

    public List<MovieDetailsDto> getTopRatedMovieDetails() throws Exception; //TopRated 상세정보들 TMDB에서 가지고 와서 DB에 저장
    public MovieDetailsDto getTopRatedMovieDetailsInDB(Long movieId, Long memberId) throws Exception; //DB에서 영화 상세정보 ID로 검색
    public MovieDetailsDto getTopRatedMovieDetailsInDBForAwards(Long movieId) throws Exception; //위와 동일. 어워즈를 위해 memberId없는 버전 제공
    public MovieDetailsDto getPosterGenre(Long movieId) throws Exception;

    public MovieDetailsDto searchMovie(String name) throws Exception; //DB에서 영화 제목으로 DB에서 검색
    public List<MovieCardDto> searchByQuery(String query);
    //public List<MovieCardDto> getMoviesByMemberId(Long memberId);
    public PageResponseDto<MovieCardDto> getAllMovieByKeyword(Long memberId, String keyword, PageRequestDto pageRequestDto);
    public Map<String, Object> getMovieMemberRecommendations(Long memberId); //~~를 보셨다면? -> 랜덤으로 추천해주기

    default MovieDetailsDto toDto(MovieDetailEntity movieDetailEntity) {
        List<MovieDetailsDto.Credits> creditDtos = movieDetailEntity.getCredits().stream()
                .map(credit -> new MovieDetailsDto.Credits(credit.getType(), credit.getName(), credit.getProfile()))
                .collect(Collectors.toList());

        List<MovieDetailsDto.Recommends> recommendDtos = movieDetailEntity.getRecommendations().stream()
                .map(recommend -> new MovieDetailsDto.Recommends(recommend.getRecommendationId()))
                .collect(Collectors.toList());

        return MovieDetailsDto.builder()
                .id(movieDetailEntity.getId())
                .title(movieDetailEntity.getTitle())
                .overview(movieDetailEntity.getOverview())
                .release_date(movieDetailEntity.getRelease_date())
                .runtime(movieDetailEntity.getRuntime())
                .images(movieDetailEntity.getImages())
                .videos(movieDetailEntity.getVideos())
                .genres(movieDetailEntity.getGenres())
                .credits(creditDtos)
                .recommendations(recommendDtos)
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
                .genres(movieDetailsDto.getGenres())
                .build();
    }

    default MovieCardDto toCardDto(MovieDetailEntity movieDetailEntity){
        return MovieCardDto.builder()
                .id(movieDetailEntity.getId())
                .title(movieDetailEntity.getTitle())
                .overview(movieDetailEntity.getOverview())
                .poster_path(movieDetailEntity.getImages())
                .release_date(movieDetailEntity.getRelease_date())
                .genre_ids(movieDetailEntity.getGenres())
                .build();
    }

    default MovieCardDto toMovieCardDto(MovieDetailEntity movieDetailEntity) {
        return MovieCardDto.builder()
                .id(movieDetailEntity.getId())
                .title(movieDetailEntity.getTitle())
                .overview(movieDetailEntity.getOverview())
                .poster_path(movieDetailEntity.getImages())
                .release_date(movieDetailEntity.getRelease_date())
                .genre_ids(movieDetailEntity.getGenres())
                .build();
    }

}