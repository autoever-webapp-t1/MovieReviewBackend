package com.movie.MovieReview.post.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.movie.MovieReview.comment.entity.Comment;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne()
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Transient
    private boolean isLiked;
    @Transient
    private Long likesCount;
    private int commentCnt;

    @OrderBy("commentId desc")
    @JsonIgnoreProperties({"post"})
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
