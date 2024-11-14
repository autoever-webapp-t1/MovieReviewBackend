package com.movie.MovieReview.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "recommendations")
public class MovieRecommendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationsId;

    @ManyToOne
    @JoinColumn(name = "id")
    private MovieDetailEntity movieDetailEntity;

    @Column(nullable = false)
    private Long recommendationId;
}
