package com.movie.MovieReview.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto implements PostDtoInterface {
    private Long postId;
    private String title;
    private String content;
    private String mainImgUrl;
    private boolean liked;
    private Integer likesCount;
    private String textContent;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public PostDto(Long postId, String title, String content, String mainImgUrl, String textContent) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.mainImgUrl = mainImgUrl;
        this.textContent = textContent;
    }

    @Override
    public String mainImgUrl() {
        return mainImgUrl;
    }

    @Override
    public String textContent() {
        return textContent;
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
    public boolean liked() {
        return liked;
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