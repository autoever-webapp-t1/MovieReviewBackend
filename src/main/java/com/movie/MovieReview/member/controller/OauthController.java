package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.dto.OauthRequestDto;
import com.movie.MovieReview.member.dto.OauthResponseDto;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.member.service.KakaoOauthService;
import com.movie.MovieReview.member.service.MemberService;
import com.movie.MovieReview.member.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin( origins = "http://localhost:5173", allowCredentials = "true")
@Log4j2
public class OauthController {
    private final OauthService oauthService;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/login/oauth/kakao")
    public ResponseEntity<OauthResponseDto> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException{
        OauthResponseDto oauthResponseDto = new OauthResponseDto();

        // KakaoOauthService에서 Access/Refresh Token 발급받기
        Map<String, Object> tokenResponse = oauthService.getKakaoToken(code);

        String accessToken = (String) tokenResponse.get("access_token");
        String refreshToken = (String) tokenResponse.get("refresh_token");

        // 응답 객체에 Access Token, Refresh Token 저장
        oauthResponseDto.setAccessToken(accessToken);
        oauthResponseDto.setRefreshToken(refreshToken);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");

        System.out.println(accessTokenCookie.getValue());
        System.out.println(refreshTokenCookie.getValue());

        response.addHeader("Set-Cookie", "accessToken=" + accessToken + "; HttpOnly; Path=/");
        response.addHeader("Set-Cookie", "refreshToken=" + refreshToken + "; HttpOnly; Path=/");

        String redirectUrl = "http://localhost:5173";
        response.sendRedirect(redirectUrl);

        return ResponseEntity.status(HttpStatus.OK).body(oauthResponseDto);
    }

    @PostMapping("/login/oauth/{provider}")
    public ResponseEntity<MemberDto> login(@PathVariable("provider") String provider,
                                           @RequestBody OauthRequestDto oauthRequestDto, HttpServletResponse response) {

        OauthResponseDto oauthResponseDto = new OauthResponseDto();
        String jwtToken = "";
        MemberDto memberDto = null; // MemberDto를 초기화합니다.

        switch (provider) {
            case "kakao":
                // loginWithKakao 메서드가 TokenResponseDto 객체를 반환한다고 가정
                jwtToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(),
                        response);
                memberDto = oauthService.UserInfo(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(),
                        response);
                break;

            default:
                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
        }

        // jwtToken을 키로, memberDto를 값으로 갖는 Map을 생성합니다.
        Map<String, MemberDto> responseMap = new HashMap<>();
        responseMap.put(jwtToken, memberDto); // jwtToken을 키로 사용

        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
    }

}