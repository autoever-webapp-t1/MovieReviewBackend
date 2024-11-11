package com.movie.MovieReview.member.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    private Long id; // 카카오 사용자 고유 아이디
    private String email; // 카카오 사용자 이메일
    private String nickname; // 카카오 사용자 이름
    private String profileImage; // 카카오 사용자 프로필 사진

    public KakaoInfoDto(Map<String, Object> attributes) {
        this.id = Long.valueOf(attributes.get("id").toString());
        this.email = attributes.get("kakao_account") != null
                ? ((Map<String, Object>) attributes.get("kakao_account")).get("email").toString()
                : "";

        this.nickname = ((Map<String, Object>) attributes.get("properties")).get("nickname").toString();
        this.profileImage = ((Map<String, Object>) attributes.get("properties")).get("profile_image").toString();

    }
}
