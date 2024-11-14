package com.movie.MovieReview.movie.dto;

import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieCreditsDto {
    private Long creditId;
    private Long movieId;
    private String name;
    private String type;
    private String profile;
}
