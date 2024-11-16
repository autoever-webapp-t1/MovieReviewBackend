package com.movie.MovieReview.Heart.service;
import com.movie.MovieReview.Heart.dao.HeartRepository;
import com.movie.MovieReview.Heart.dto.HeartRequestDto;
import com.movie.MovieReview.Heart.entity.Heart;
import com.movie.MovieReview.exception.NotFoundException;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
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
    private UserPrincipal userPrincipal;

    private MemberEntity getLoginMember() {
        String loginMemberEmail = userPrincipal.getEmail();
        return memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    @Transactional
    public void insert(HeartRequestDto heartRequestDto) throws Exception {
        MemberEntity member =getLoginMember();
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
    public void delete(HeartRequestDto heartRequestDto) {
        MemberEntity member = getLoginMember();

        Post post = postRepository.findById(heartRequestDto.getPostId())
                .orElseThrow(()-> new NotFoundException("Could not found board id : " + heartRequestDto.getPostId()));
        Heart heart = heartRepository.findByMemberAndPost(member,post)
                .orElseThrow(()->new NotFoundException("Could not found heart id"));
        heartRepository.delete(heart);
        postRepository.decrementLikeCount(post.getPostId());
    }
}