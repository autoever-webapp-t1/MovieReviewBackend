package com.movie.MovieReview.member.service;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kakao.key.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    //카카오 사용자 토큰 받아오기
    public Map<String, Object>  getKakaoToken(String code) {
        WebClient webClient = WebClient.builder().baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).build();

        return webClient.post().uri("/oauth/token")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", kakaoClientId)
                        .with("redirect_uri", kakaoRedirectUri).with("code", code))
                .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                }).block();
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

    //사용자 정보 가져와서 db에 저장
    public MemberDto UserInfo(String accessToken, String refreshToken, HttpServletResponse response) {
        MemberDto memberDto = kakaoOauthService.getUserProfileByToken(accessToken, refreshToken);
        return memberDto;
    }
//    // 리프레시 토큰으로 액세스토큰 새로 갱신
//    public String refreshAccessToken(String refreshToken) {
//        MemberDto memberDto = memberService.findByRefreshToken(refreshToken);
//        if(memberDto == null) {
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        if(!jwtTokenService.validateToken(refreshToken)) {
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        return jwtTokenService.createAccessToken(memberDto.getMemberId().toString());
//    }
}