package com.movie.MovieReview.post.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.movie.MovieReview.comment.entity.Comment;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    private MemberEntity writer;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Column(name = "isLiked")
    private boolean isLiked;

    @Column(name="likesCount")
    private int likesCount;

    @Setter
    private int commentCnt;

    @OrderBy("commentId desc")
    @JsonIgnoreProperties({"post"})
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(MemberEntity writer, String title, String content) {
        this.writer = writer;
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        incrementCommentCount();
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
        decrementCommentCount();
    }

    public void incrementCommentCount() {
        this.commentCnt++;
    }

    public void decrementCommentCount() {
        if (this.commentCnt > 0) {
            this.commentCnt--;
        }
    }

    public void update(PostResDto dto) {
        if(this.postId!= dto.getPostId()) {
            throw new IllegalArgumentException("게시글 수정 실패. 잘못된 id가 입력되었습니다.");
        }
        if(dto.getTitle() != null)
            this.title = dto.getTitle();
        if (dto.getContent() != null)
            this.content = dto.getContent();
    }
}
