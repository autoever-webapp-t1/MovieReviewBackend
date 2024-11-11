package com.movie.MovieReview.member.service;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthService {
    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;
    private final JwtTokenService jwtTokenService;

    //카카오 사용자 토큰 받아오기
    public Map<String, Object> getKakaoToken(String code) {
        WebClient webClient = WebClient.builder().baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).build();

        return webClient.post().uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", "d8fabac493f22b719a1bc4f29b44c9d1")
                        .with("redirect_uri", "http://localhost:8080/login/oauth/kakao").with("code", code))
                .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                }).block();

    }

    public String loginWithKakao(String accessToken, String refreshToken, HttpServletResponse response) {
        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);

        if (memberDto == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        // 자체적인 JWT 토큰 생성
        String JWTToken = jwtTokenService.createJWTToken(accessToken, refreshToken,
                String.valueOf(memberDto.getMemberId()));

        // TokenResponseDto 생성
        TokenResponseDto tokenResponseDto = new TokenResponseDto();
        tokenResponseDto.setAccessToken(accessToken);
        tokenResponseDto.setRefreshToken(refreshToken);
        return JWTToken; // TokenResponseDto 객체 반환
    }

    //카카오 사용자 로그아웃
    public Map<String, Object> logout(String accessToken) {
        WebClient webClient = WebClient.builder().baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // Access Token을 Authorization 헤더에 추가
                .build();

        // 로그아웃 요청
        Map<String, Object> logoutResponse = webClient.post().uri("/v1/user/logout").retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                }).block();
        return logoutResponse; // 로그아웃 결과 응답
    }

    public MemberDto UserInfo(String accessToken, String refreshToken, HttpServletResponse response) {
        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);
        return memberDto;
    }
}
