package com.movie.MovieReview.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {
    private Long memberId; //카카오 고유 사용자 ID

    private String nickname; //카카오 사용자 이름

    private String email; //카카오 사용자 이메일

    private String profile; //카카오 사용자 프로필사진

    private String refreshToken; //카카오 사용자 refreshToken

    private boolean isExisted; //로그인 한 적 있다 -> True(세개 선택한 적 있음), 로그인 처음이다 -> False(세개 선택해야함)
}
