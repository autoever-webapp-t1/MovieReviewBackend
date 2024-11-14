package com.movie.MovieReview.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.post.dto.PostDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReqDto {
    private Long commentId;
    private String content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PostDto postDto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MemberDto memberDto;
    public CommentReqDto(String content) {
        this.content = content;
    }
}
