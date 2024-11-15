package com.movie.MovieReview.awards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "awards")
public class AwardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long awardsId;

    private int status;

    private String awardName;

    private Long nominated1;

    private Long nominated2;

    private Long nominated3;

    private Long nominated4;

    private Date startDateTime;

    private Date endDateTime;

    private Long topMovieId; //일등 작품의 movieId
}
