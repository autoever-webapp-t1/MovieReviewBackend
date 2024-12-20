package com.movie.MovieReview.comment.entity;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.post.entity.Post;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Comment(MemberEntity writer, Post post, String content) {
        this.writer = writer;
        this.post = post;
        this.content = content;
    }

    public void updateContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }

}