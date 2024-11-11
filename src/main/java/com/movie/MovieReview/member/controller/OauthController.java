//package com.movie.MovieReview.member.controller;
//
//import com.movie.MovieReview.exception.CustomException;
//import com.movie.MovieReview.exception.ErrorCode;
//import com.movie.MovieReview.member.dto.MemberDto;
//import com.movie.MovieReview.member.dto.OauthRequestDto;
//import com.movie.MovieReview.member.dto.OauthResponseDto;
//import com.movie.MovieReview.member.dto.RefreshTokenResponseDto;
//import com.movie.MovieReview.member.service.JwtTokenService;
//import com.movie.MovieReview.member.service.KakaoOauthService;
//import com.movie.MovieReview.member.service.MemberService;
//import com.movie.MovieReview.member.service.OauthService;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin("*")
//@Log4j2
//public class OauthController {
//    private final OauthService oauthService;
//    private final MemberService memberService;
//    private final JwtTokenService jwtTokenService;
//
//    @GetMapping("/login/oauth/kakao")
//    public ResponseEntity<OauthResponseDto> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException{
//        OauthResponseDto oauthResponseDto = new OauthResponseDto();
//
//        // KakaoOauthService에서 Access/Refresh Token 발급받기
//        Map<String, Object> tokenResponse = oauthService.getKakaoToken(code);
//
//        log.info("OauthController: ????????????" );
//
//        String accessToken = (String) tokenResponse.get("access_token");
//        String refreshToken = (String) tokenResponse.get("refresh_token");
//
//        // 응답 객체에 Access Token, Refresh Token 저장
//        oauthResponseDto.setAccessToken(accessToken);
//        oauthResponseDto.setRefreshToken(refreshToken);
//
//        return ResponseEntity.status(HttpStatus.OK).body(oauthResponseDto);
//    }
//
//    @PostMapping("/login/oauth/{provider}")
//    public ResponseEntity<MemberDto> login(@PathVariable("provider") String provider,
//                                           @RequestBody OauthRequestDto oauthRequestDto, HttpServletResponse response) {
//
//        OauthResponseDto oauthResponseDto = new OauthResponseDto();
//        String jwtToken = "";
//        MemberDto memberDto = null; // MemberDto를 초기화합니다.
//
//        switch (provider) {
//            case "kakao":
//                // loginWithKakao 메서드가 TokenResponseDto 객체를 반환한다고 가정
//                jwtToken = oauthService.loginWithKakao(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(),
//                        response);
//                memberDto = oauthService.UserInfo(oauthRequestDto.getAccessToken(), oauthRequestDto.getRefreshToken(),
//                        response);
//                break;
//
//            default:
//                throw new IllegalArgumentException("#OauthController: Unsupported provider: " + provider);
//        }
//
//        // jwtToken을 키로, memberDto를 값으로 갖는 Map을 생성합니다.
//        Map<String, MemberDto> responseMap = new HashMap<>();
//        responseMap.put(jwtToken, memberDto); // jwtToken을 키로 사용
//
//        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
//    }
//
//    // 리프레시 토큰으로 액세스토큰 재발급 받는 로직
//    @PostMapping("/token/refresh")
//    public RefreshTokenResponseDto tokenRefresh(HttpServletRequest request) {
//        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
//        Cookie[] list = request.getCookies();
//        if(list == null) {
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//
//        Cookie refreshTokenCookie = Arrays.stream(list).filter(cookie -> cookie.getName().equals("refresh_token")).collect(Collectors.toList()).get(0);
//
//        if(refreshTokenCookie == null) {
//            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
//        }
//        String accessToken = oauthService.refreshAccessToken(refreshTokenCookie.getValue());
//        refreshTokenResponseDto.setAccessToken(accessToken);
//        return refreshTokenResponseDto;
//    }
//}
