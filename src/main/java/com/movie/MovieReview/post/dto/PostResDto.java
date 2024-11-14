package com.movie.MovieReview.post.dto;

import com.movie.MovieReview.post.entitiy.Post;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class PostResDto implements PostDtoInterface{
    private Long postId;
    private String nickname;
    private String title;
    private String content;
    private Integer commentCnt;
    private boolean isLiked;
    private Integer likesCount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static PostResDto entityToResDto(Post post) {
        return PostResDto.builder()
                .postId(post.getPostId())
                .nickname(post.getWriter().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCnt(post.getCommentCnt())
                .isLiked(post.isLiked())
                .likesCount(post.getLikesCount())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }
    @Override
    public Long getPostId() {
        return postId;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public boolean isLiked() {
        return isLiked;
    }

    @Override
    public Integer likesCount() {
        return likesCount;
    }

    @Override
    public LocalDateTime createdDate() {
        return createdDate;
    }

    @Override
    public LocalDateTime modifiedDate() {
        return modifiedDate;
    }
}
