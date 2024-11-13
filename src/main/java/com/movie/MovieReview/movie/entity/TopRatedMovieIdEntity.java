package com.movie.MovieReview.movie.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TopRatedMovieId")
public class TopRatedMovieIdEntity {
    @Id
    Long id; //movie id
}
