package com.movie.MovieReview.awards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "awards")
public class AwardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long awardId;

    private String awardName;

    private int nominated1;

    private int nominated2;

    private int nominated3;

    private int nominated4;

    private Date startDateTime;

    private Date endDateTime;
}
