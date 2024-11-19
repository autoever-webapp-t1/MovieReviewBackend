package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.awards.dto.AwardsDto;
import com.movie.MovieReview.awards.service.AwardsService;
import com.movie.MovieReview.member.dto.*;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.member.service.KakaoOauthService;
import com.movie.MovieReview.member.service.MemberService;
import com.movie.MovieReview.member.service.OauthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://gasanne.site"}, allowCredentials = "true")
@Log4j2
public class OauthController {
    private final OauthService oauthService;
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final AwardsService awardsService;
    private final MemberRepository memberRepository;

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

        response.addHeader("Set-Cookie", "accessToken=" + accessToken + "; HttpOnly; Path=/; Domain=localhost; SameSite=None");
        response.addHeader("Set-Cookie", "refreshToken=" + refreshToken + "; HttpOnly; Path=/; Domain=localhost SameSite=None;");

//        String redirectUrl = "http://localhost:5173";
//        response.sendRedirect(redirectUrl);

        return ResponseEntity.status(HttpStatus.OK).body(oauthResponseDto);
    }

    @PostMapping("/login/oauth/kakao")
    public ResponseEntity<?> login(@RequestBody OauthRequestDto oauthRequestDto) {
        JwtWithMemberDto jwtWithMemberDto = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken());

        String jwtToken = jwtWithMemberDto.getJwtToken();
        MemberDto memberDto = jwtWithMemberDto.getMemberDto();

        System.out.println("OauthController jwtToken??????? : "+ jwtToken);

        System.out.println("OauthController memberDto??????? : "+ memberDto);

        MemberDto dto = MemberDto.builder()
                .memberId(memberDto.getMemberId())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .profile(memberDto.getProfile())
                .refreshToken(oauthRequestDto.getRefreshToken())
                .existed(memberDto.isExisted()).build();

        System.out.println("OauthController dto??????? : "+ dto);

        MemberEntity memberEntity = memberService.toEntity(dto);

        memberService.save(memberEntity);

        AwardsDto awardsDto = awardsService.getCurrentAwards(); //현재 시상식 정보

        System.out.println("OauthController awardsDto??????? : "+ awardsDto);

        MemberAwardsResponseDto responseDto = MemberAwardsResponseDto.builder()
                .member(memberDto)
                .award(awardsDto)
                .jwtToken(jwtToken)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}