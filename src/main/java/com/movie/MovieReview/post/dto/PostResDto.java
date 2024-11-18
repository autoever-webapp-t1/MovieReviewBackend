package com.movie.MovieReview.post.dto;

import com.movie.MovieReview.post.entity.Post;
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
    private boolean liked;
    private Integer likesCount;
    private String profile;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static PostResDto entityToResDto(Post post) {
        return PostResDto.builder()
                .postId(post.getPostId())
                .nickname(post.getWriter().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCnt(post.getCommentCnt())
                .liked(post.isLiked())
                .likesCount(post.getLikesCount())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .profile(post.getWriter().getProfile())
                .build();
    }

    public String getNickname() {return nickname;}
    public boolean getIsLiked() {return liked;}
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
