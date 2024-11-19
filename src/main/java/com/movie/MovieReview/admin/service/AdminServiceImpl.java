package com.movie.MovieReview.admin.service;

import com.movie.MovieReview.admin.dto.AwardAddMoviesDto;
import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.awards.repository.AwardsRepository;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.enums.MemberRole;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AwardsRepository awardsRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenService jwtTokenService;

    private Long extractMemberId(String authorizationHeader) throws Exception {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("클라이언트에서 헤더 토큰 오류!!!!!");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtTokenService.validateToken(token)) {
            throw new IllegalArgumentException("유효하지 않은 토큰!!!!");
        }

        return Long.valueOf(jwtTokenService.getPayload(token));
    }

    private Long getLoginMember(String authorizationHeader) throws Exception {
        Long memberId = extractMemberId(authorizationHeader);
        return memberId;
    }

    @Transactional
    public AwardsEntity addAwardWithMovies(String authorizationHeader, AwardAddMoviesDto dto) throws Exception {
        Long memberId = getLoginMember(authorizationHeader);
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        if(!member.getMemberRole().equals(MemberRole.ADMIN)){
            throw new AccessDeniedException("Access Denied: Admin role required.");
        }
        AwardsEntity award = new AwardsEntity();
        award.setAwardName(dto.getAwardName());
        Long[] movieIds = dto.getMovieIds();
        for (int i=0; i<movieIds.length; i++) {
            if (movieIds[i]!=null) {
                switch (i) {
                    case 0:
                        award.setNominated1(movieIds[i]);
                        break;
                    case 1:
                        award.setNominated2(movieIds[i]);
                        break;
                    case 2:
                        award.setNominated3(movieIds[i]);
                         break;
                    case 3:
                        award.setNominated4(movieIds[i]);
                        break;
                }
            }
        }
        return awardsRepository.save(award);
    }
}
