package com.movie.MovieReview.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    //private Double totalAverageSkill; // 영화 육각형 통계의 평균

    @Data
    public static class Genres {
        private int id;
    }
}
