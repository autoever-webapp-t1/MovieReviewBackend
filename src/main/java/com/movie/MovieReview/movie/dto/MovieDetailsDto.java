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
public class MovieDetailsDto {
    private Long id; //
    private String title;
    private String overview;
    private String release_date;
    private int runtime;
    private String images;
    private String videos;
    private String genres;

    private List<Credits> credits;
    private List<Recommends> recommendations;

    private Map<String, Object> score; // 평균 스킬 데이터 + totalAvgSkill을 저장할 score 필드
    private Map<String, Object> myScore; //내가 리뷰한 score

    private List<String> awardsNames; // 수상 경력 있으면 awardsName 보여주기


    public MovieDetailsDto(Long id, String title, String overview, String release_date, int runtime, String images,
                           String videos, String genres, List<Credits> credits, List<Recommends> recommendations) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.runtime = runtime;
        this.images = images;
        this.videos = videos;
        this.genres = genres;
        this.credits = credits;
        this.recommendations = recommendations;
    }

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

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Credits {
        private String type;
        private String name;
        private String profile;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Recommends{
        private Long id;
    }
}
