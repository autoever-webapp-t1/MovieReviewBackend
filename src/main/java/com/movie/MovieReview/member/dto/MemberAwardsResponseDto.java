package com.movie.MovieReview.member.dto;

import com.movie.MovieReview.awards.dto.AwardsDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberAwardsResponseDto {
    private MemberDto member;
    private AwardsDto award;
    private String jwtToken;
}
