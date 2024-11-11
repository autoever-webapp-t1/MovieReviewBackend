//package com.movie.MovieReview.comment.domain;
//
//import com.movie.MovieReview.member.entity.MemberEntity;
////import com.movie.MovieReview.post.entitiy.Post;
//import com.movie.global.entity.BaseTimeEntity;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Comment extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "comment_id")
//    private Long commentId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private MemberEntity writer;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    @Column(columnDefinition = "text", nullable = false)
//    private String content;
//
//    @Builder
//    public Comment(MemberEntity writer, Post post, String content) {
//        this.writer = writer;
//        this.post = post;
//        this.content = content;
//    }
//}
