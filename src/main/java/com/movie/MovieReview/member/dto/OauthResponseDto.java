package com.movie.MovieReview.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OauthResponseDto {
    private String accessToken;
    private String refreshToken;
}
