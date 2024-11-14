package com.movie.MovieReview.post.dto;

import java.time.LocalDateTime;

public class PostDetailDto implements PostDtoInterface{
    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private String nickname;
    private Integer likesCount;
    private boolean isLiked;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
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
        return updatedDate;
    }
}
