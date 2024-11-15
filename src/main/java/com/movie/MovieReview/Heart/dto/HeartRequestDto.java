package com.movie.MovieReview.Heart.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartRequestDto {
    private Long memberId;
    private Long postId;
    public HeartRequestDto(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }
}