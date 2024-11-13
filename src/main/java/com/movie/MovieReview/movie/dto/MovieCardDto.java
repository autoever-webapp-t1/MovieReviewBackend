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
    private String release_date; //movie 개봉 날ㅉ
    private List<Integer> genre_ids; // movie 장르 ID들
}
