package com.movie.MovieReview.comment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReqDto {
    private String content;
    public CommentReqDto(String content) {
        this.content = content;
    }
}
