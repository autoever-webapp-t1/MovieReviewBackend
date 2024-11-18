package com.movie.MovieReview.member.service;

import com.movie.MovieReview.member.dto.KakaoInfoDto;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Transactional
@PersistenceContext
@Service
@RequiredArgsConstructor
public class KakaoOauthService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private EntityManager entityManager;

    // 카카오 API 호출해서 AccessToken으로 유저정보 가져오기
    public Map<String, Object> getUserAttributesByToken(String accessToken) {
        return WebClient.create().get().uri("https://kapi.kakao.com/v2/user/me")
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken)).retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                }).block();
    }

    // 카카오 API에서 가져온 유저정보를 DB에 저장
    public MemberDto getUserProfileByToken(String accessToken, String refreshToken) {
        //entityManager.clear();
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);

        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);

        Boolean isExisted = memberRepository.findById(kakaoInfoDto.getId()).isPresent();

        MemberDto memberDto = MemberDto.builder().memberId(kakaoInfoDto.getId()).email(kakaoInfoDto.getEmail())
                .nickname(kakaoInfoDto.getNickname()).profile(kakaoInfoDto.getProfileImage()).refreshToken(refreshToken).existed(isExisted).build();

        MemberEntity memberEntity = memberService.toEntity(memberDto);

        memberService.save(memberEntity);

        return memberDto;
    }

    public MemberDto getUserProfileByTokenNoSave(String accessToken, String refreshToken) {
        Map<String, Object> userAttributesByToken = getUserAttributesByToken(accessToken);

        KakaoInfoDto kakaoInfoDto = new KakaoInfoDto(userAttributesByToken);

        Boolean isExisted = memberRepository.findById(kakaoInfoDto.getId()).isPresent();

        MemberDto memberDto = MemberDto.builder().memberId(kakaoInfoDto.getId()).email(kakaoInfoDto.getEmail())
                .nickname(kakaoInfoDto.getNickname()).profile(kakaoInfoDto.getProfileImage()).refreshToken(refreshToken).existed(isExisted).build();


        return memberDto;
    }
}
