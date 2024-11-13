package com.movie.MovieReview.movie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "credits")
public class MovieCreditsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditId;

    @ManyToOne
    @JoinColumn(name = "id")
    private MovieDetailEntity movieDetailEntity;

    private String name;
    private String type;
    private String profile;

}
