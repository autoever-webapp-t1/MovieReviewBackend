package com.movie.MovieReview.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailsDto {
    private Long id; //
    private String title;
    private String overview;
//    private List<String> images;
//    private List<String> videos;
    private String release_date;
    private int runtime;
    //private List<Integer> genres;
}
