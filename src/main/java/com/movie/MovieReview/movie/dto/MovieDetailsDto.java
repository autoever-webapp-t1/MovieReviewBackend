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
public class MovieDetailsDto {
    private Long id; //
    private String title;
    private String overview;
    private String release_date;
    private int runtime;
    private String images;
    private String videos;
    //private List<Genres> genres;
//    private List<String> recommendations;

    @Data
    public static class Images{
        private String poster_path;
        private String backdrop_path;
    }

    @Data
    public static class Videos{
        private String key;
        private String type;
    }

    @Data
    public static class Genres {
        private int id;
        private String name;
    }
}
