package com.movie.MovieReview.member.entity;

import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wish")
public class WishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    @ManyToOne
    @JoinColumn(name = "id")
    private MovieDetailEntity movie;

    @ManyToOne
    @JoinColumn(name="member_id")
    private MemberEntity member;
}
