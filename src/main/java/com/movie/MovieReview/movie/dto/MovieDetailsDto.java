package com.movie.MovieReview.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailsDto {
    private Long id; //
    private String title;
    private String overview;
    private List<Images> images;
//    private List<String> videos;
    private String release_date;
    private int runtime;
    private List<Genre> genres;
//    private List<String> recommendations;

    @Data
    public static class Images{
        private String poster_path;
        private String backdrop_path;
    }

    @Data
    public static class Genre {
        private int id;
        private String name;
    }
}
