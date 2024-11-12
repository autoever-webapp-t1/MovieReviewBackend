package com.movie.MovieReview.comment.entity;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity writer;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;
}