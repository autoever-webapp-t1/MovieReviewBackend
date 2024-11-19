package com.movie.MovieReview.Heart.entity;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "heart")
public class Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Heart(MemberEntity member, Post post) {
        this.member = member;
        this.post = post;
    }
}