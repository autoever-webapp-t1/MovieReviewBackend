package com.movie.MovieReview.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
