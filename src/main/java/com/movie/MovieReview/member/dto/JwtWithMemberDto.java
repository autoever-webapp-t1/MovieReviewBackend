package com.movie.MovieReview.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtWithMemberDto {
    private String JwtToken;
    private MemberDto memberDto;
}
