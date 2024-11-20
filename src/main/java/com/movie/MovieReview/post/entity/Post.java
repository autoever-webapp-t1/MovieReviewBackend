package com.movie.MovieReview.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.movie.MovieReview.comment.entity.Comment;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "BLOB", nullable = false)
    private String content;

    @Column(name = "liked")
    private boolean liked;

    @Column(name="likesCount")
    private int likesCount;

    @Setter
    private int commentCnt;

    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String textContent;

    // thumbnail
    private String mainImgUrl;

    @OrderBy("commentId desc")
    @JsonIgnoreProperties({"post"})
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Post(MemberEntity member, String title, String content, String mainImgUrl, String textContent) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.mainImgUrl = mainImgUrl;
        this.textContent = textContent;
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

    public void update(String title, String content, String mainImgUrl) {
      this.title = title;
      this.content = content;
      this.mainImgUrl = mainImgUrl;
    }
}
