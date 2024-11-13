package com.movie.MovieReview.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "MovieDetail")
public class MovieDetailEntity {
    @Id
    private Long id; //movie id

    private String title; //movie제목

    @Column(columnDefinition = "LONGTEXT")
    private String overview; //movie 줄거리

    private String release_date; //movie 개봉 날짜

    private int runtime; //movie 런타임

    @Column(columnDefinition = "TEXT")
    private String images; // movie 이미지 리스트

    @Column(columnDefinition = "TEXT")
    private String videos; // movie 비디오 리스트
}
