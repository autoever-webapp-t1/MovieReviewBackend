package com.movie.MovieReview.post.service;

import com.movie.MovieReview.member.dto.KakaoInfoDto;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.post.dto.PostDetailDto;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.post.exception.PostNotFoundException;
import com.movie.MovieReview.post.repository.PostRepository;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    PostRepository postRepository;
//    UserPrincipal userPrincipal;
    MemberRepository memberRepository;
    KakaoInfoDto kakaoInfoDto;
    private final SecurityUtils securityUtils;


    private MemberEntity getLoginMember() {
//        String loginMemberEmail = userPrincipal.getEmail();
        String loginMemberEmail = kakaoInfoDto.getEmail();
//        String loginMemberEmail = securityUtils.getLoginMemberEmail();
        return memberRepository.findByEmail(loginMemberEmail)
                .orElseThrow(()->new RuntimeException("member not found"));
    }

    @Override
    public Post findByPostId(Long postId) {
        try {
            return postRepository.findById(postId)
                    .orElseThrow(()->new RuntimeException("board not found with id"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error while finding post by id",e);
        }
    }

    @Override
    public PostResDto createPost(PostDto postDto) {
        MemberEntity member = getLoginMember();
        String title = postDto.getTitle();
        String content = postDto.content();
        Post post = Post.builder()
                .writer(member)
                .title(title)
                .content(content)
                .build();

        postRepository.save(post);
        return PostResDto.entityToResDto(post);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        MemberEntity member = getLoginMember();
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException());
        if (!post.getWriter().equals(member)) {
            throw new RuntimeException("접근 권한이 없습니다");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public PostResDto updatePost(Long postId, PostResDto postDto) {
        MemberEntity member = getLoginMember();
        Post targetPost = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException());
        if (!targetPost.getWriter().equals(member)) {
            throw new RuntimeException("접근 권한이 없습니다");
        }
        targetPost.update(postDto);

        Post updatedPost = postRepository.save(targetPost);
        return PostResDto.entityToResDto(targetPost);
    }

    @Override
    public List<PostResDto> findPostByMemberId(Long memberId) {
        return List.of();
    }

    @Override
    public PostDetailDto getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("post not found"));
        return postDetailDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<PostDetailDto> getAllPosts(PageRequestDto pageRequestDto) {
        PageRequest pageable = PageRequest.of(pageRequestDto.getPage()-1,pageRequestDto.getSize());
        Page<Post> postPage = (Page<Post>) postRepository.findAll();
        List<PostDetailDto> posts = postPage.getContent().stream()
                .map(this::postDetailDto)
                .collect(Collectors.toList());

        return PageResponseDto.<PostDetailDto>withAll()
                .dtoList(posts)
                .pageRequestDto(pageRequestDto)
                .total(postPage.getTotalElements())
                .build();
    }


    public PostDetailDto postDetailDto(Post post) {
        return PostDetailDto.builder()
                .postId(post.getPostId())
                .memberId(post.getWriter().getMemberId())
                .title(post.getTitle())
                .content(post.getContent())
                .nickname(post.getWriter().getNickname())
                .likesCount(post.getLikesCount())
                .liked(post.isLiked())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .build();
    }

//    @Override
//    public Page<Post> searchByTitle(String title, int page, int size) {
//        if (title == null)
//            title = "";
//        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by("postId").descending());
//        Page<Post> byTitleContaining = postRepository.findByTitleContaining(title,pageRequest);
//        return byTitleContaining;
//    }
}
