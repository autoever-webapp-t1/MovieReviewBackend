package com.movie.MovieReview.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieCardDto {
    private Long id; //movie id
    private String title; //movie제목
    private String overview; //movie 줄거리
    private String poster_path; //movie 포스터 url https://image.tmdb.org/t/p/w500/poster_path
    private String release_date; //movie 개봉 날짜
    private String genre_ids; // movie 장르 ID들
    private Map<String, Object> score; // 평균 스킬 데이터 + totalAvgSkill을 저장할 score 필드
    private Map<String, Object> myScore; //내가 리뷰한 score

    @Data
    public static class Genres {
        private int id;
    }

    public MovieCardDto(Long id, String title, String overview, String posterPath, String releaseDate, String genreIds) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.poster_path = posterPath;
        this.release_date = releaseDate;
        this.genre_ids = genreIds;
    }
}
