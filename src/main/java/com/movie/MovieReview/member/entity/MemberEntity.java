package com.movie.MovieReview.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MemberEntity {
    @Id
    private Long memberId;
}
