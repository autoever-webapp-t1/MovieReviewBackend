package com.movie.MovieReview.comment.entity;

import com.movie.MovieReview.comment.dto.CommentResDto;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
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


    public void patch(CommentResDto dto) {
        if(this.commentId != dto.getCommentId()) {
            throw new IllegalArgumentException("댓글 수정 실패. 잘못된 id가 입력되었습니다.");
        }
        if (dto.getContent() != null)
            this.content = dto.getContent();
    }

}
