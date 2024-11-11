package com.movie.MovieReview.post.dto;

import com.movie.MovieReview.post.entitiy.Post;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PostResDto {
    private final Long postId;
    private final String writerNickname;
    private final String title;
    private final String content;
    private final Integer commentCnt;
    private final LocalDate createdAt;
    private final LocalDate modifiedAt;

    public static PostResDto entityToResDto(Post post) {
        return PostResDto.builder()
                .postId(post.getPostId())
//                .writerNickname(post.getAuthor().getNickName())
                .title(post.getTitle())
                .content(post.getContent())
//                .commentCnt(post.getCommentCnt())
                .createdAt(LocalDate.from(post.getCreatedDate()))
                .modifiedAt(LocalDate.from(post.getModifiedDate()))
                .build();
    }
}
