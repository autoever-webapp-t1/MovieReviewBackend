package com.movie.MovieReview.member.service;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // MemberEntity를 DTO로 변환
    private MemberDto toDto(MemberEntity memberEntity) {
        return MemberDto.builder().memberId(memberEntity.getMemberId()).email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname()) // 필드명 수정
                .profile(memberEntity.getProfile())
                .refreshToken(memberEntity.getRefreshToken()).build();
    }

    // MemberDto를 MemberEntity로 변환
    public MemberEntity toEntity(MemberDto memberDto) {
        return MemberEntity.builder().memberId(memberDto.getMemberId()).email(memberDto.getEmail())
                .nickname(memberDto.getNickname()).profile(memberDto.getProfile())
                .refreshToken(memberDto.getRefreshToken()).isExisted(memberDto.isExisted()).build();
    }

    // memberId로 찾은 후 DTO로 변환
    public MemberDto findById(Long memberId) {
        return memberRepository.findById(memberId).map(this::toDto).orElseThrow(() -> {
            System.err.println("#MemberService: Member not found for memberId: " + memberId);
            return new CustomException(ErrorCode.BAD_REQUEST);
        });
    }

    // refreshToken으로 Member를 찾아서 DTO로 반환
    public MemberDto findByRefreshToken(String refreshToken) {
        return memberRepository.findByRefreshToken(refreshToken)
                .map(this::toDto)
                .orElseThrow(() -> {
                    System.out.println("#MemberService No member found for refresh token: " + refreshToken);
                    return new CustomException(ErrorCode.BAD_REQUEST);
                });
    }

    // Optional<MemberEntity>를 MemberDto로 변환하는 메서드
    public MemberDto getMemberDtoFromRefreshToken(String refreshToken) {
        System.out.println("#MemberService Received refresh token: " + refreshToken); // 리프레시 토큰을 출력

        return memberRepository.findByRefreshToken(refreshToken).map(memberEntity -> {
            System.out.println("#MemberService Found member entity for refresh token: " + memberEntity); // 멤버 엔티티가 발견된
            // 경우 출력
            return toDto(memberEntity);
        }).orElseThrow(() -> {
            System.out.println("#MemberService No member found for refresh token: " + refreshToken); // 멤버가 발견되지 않은 경우
            // 출력
            return new CustomException(ErrorCode.BAD_REQUEST);
        });
    }

    @Transactional
    public MemberEntity save(MemberEntity memberEntity) {
        try {
            System.out.println("Saving member: " + memberEntity);
            return memberRepository.save(memberEntity);
        } catch (Exception e) {
            System.err.println("Error saving member: " + e.getMessage());
            throw e; // 예외를 다시 던져서 호출자에게 전달
        }
    }

    // 존재하면 업데이트, 없으면 삽입
    public MemberEntity update(MemberEntity memberEntity) {
        return memberRepository.save(memberEntity);
    }

    // refreshToken 업데이트 메서드
    @Transactional
    public void updateRefreshToken(Long memberId, String refreshToken) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));

        memberEntity.setRefreshToken(refreshToken);
        memberRepository.save(memberEntity); // 변경된 엔티티 저장
    }

    public void saveTokens(Long memberId, String accessToken, String refreshToken) {
        Optional<MemberEntity> memberOptional = memberRepository.findById(memberId);

        MemberEntity memberEntity;
        if (memberOptional.isPresent()) {
            memberEntity = memberOptional.get();
        } else {
            memberEntity = MemberEntity.builder().memberId(memberId).build();
        }

    }

    @Transactional
    public void updateNickname(Long memberId, String newNickname) {

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        member.setNickname(newNickname);
        memberRepository.save(member);
    }

}
