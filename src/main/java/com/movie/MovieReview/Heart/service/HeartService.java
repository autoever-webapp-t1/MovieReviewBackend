package com.movie.MovieReview.Heart.service;
import com.movie.MovieReview.Heart.dao.HeartRepository;
import com.movie.MovieReview.Heart.dto.HeartRequestDto;
import com.movie.MovieReview.Heart.entity.Heart;
import com.movie.MovieReview.exception.NotFoundException;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
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

    private MemberEntity getLoginMember(String authorizationHeader) throws Exception {
        Long memberId = extractMemberId(authorizationHeader);
        return memberRepository.findById(memberId)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    @Transactional
    public void insert(String authorizationHeader, HeartRequestDto heartRequestDto) throws Exception {
        MemberEntity member =getLoginMember(authorizationHeader);
        Post post = postRepository.findById(heartRequestDto.getPostId())
                .orElseThrow(()-> new NotFoundException("Could not found post id: " + heartRequestDto.getPostId()));
        if(heartRepository.findByMemberAndPost(member,post).isPresent()) {
            throw new Exception();
        }
        Heart heart = Heart.builder()
                .post(post)
                .member(member)
                .build();
        heartRepository.save(heart);
        postRepository.incrementLikeCount(post.getPostId());
    }
    @Transactional
    public void delete(String authorizationHeader, HeartRequestDto heartRequestDto) throws Exception {
        MemberEntity member = getLoginMember(authorizationHeader);

        Post post = postRepository.findById(heartRequestDto.getPostId())
                .orElseThrow(()-> new NotFoundException("Could not found board id : " + heartRequestDto.getPostId()));
        Heart heart = heartRepository.findByMemberAndPost(member,post)
                .orElseThrow(()->new NotFoundException("Could not found heart id"));
        heartRepository.delete(heart);
        postRepository.decrementLikeCount(post.getPostId());
    }
}