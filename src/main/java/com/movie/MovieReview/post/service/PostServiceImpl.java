package com.movie.MovieReview.post.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.service.JwtTokenService;

import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.post.exception.NoPostsFoundException;
import com.movie.MovieReview.post.exception.PostNotFoundException;
import com.movie.MovieReview.post.repository.PostRepository;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;

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
    public PostResDto createPost(String authorizationHeader, PostDto postDto) throws Exception {
        Long memberId = getLoginMember(authorizationHeader);
        MemberEntity member = memberRepository.findById(memberId).orElseThrow();
        String title = postDto.getTitle();
        String content = postDto.getContent();
        String mainImgUrl = postDto.getMainImgUrl();
        String textContent = postDto.getTextContent();
        Post post = Post.builder()
                .writer(member)
                .title(title)
                .mainImgUrl(mainImgUrl)
                .textContent(textContent)
                .content(content)
                .build();

        postRepository.save(post);
        return PostResDto.entityToResDto(post);
    }

    @Override
    @Transactional
    public void deletePost(String authorizationHeader, Long postId) throws Exception {
        Long memberId = getLoginMember(authorizationHeader);
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException());


        if (post.getWriter() == null || !memberId.equals(post.getWriter().getMemberId()))  {
            throw new RuntimeException("접근 권한이 없습니다");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public PostResDto updatePost(Long postId, PostDto postDto) throws Exception {
        Post targetPost = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("대상 포스트가 없습니다."));
        targetPost.update(postDto.getTitle(),postDto.getContent(),postDto.getMainImgUrl());
        return PostResDto.entityToResDto(targetPost);
    }

    @Override
    public List<PostResDto> findPostByMemberId(Long memberId) {
        return List.of();
    }

    @Override
    public PostResDto getPost(Long postId) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("post not found"));
        return PostResDto.entityToResDto(post);
    }

    public PageResponseDto<PostResDto> getAllPosts(PageRequestDto pageRequestDto) {
        PageRequest pageable = PageRequest.of(pageRequestDto.getPage()-1, pageRequestDto.getSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Post> postPage = postRepository.findAll(pageable);
        List<PostResDto> posts = postPage.getContent().stream().map(PostResDto::entityToResDto)
                .collect(Collectors.toList());
        if (posts.isEmpty()) {
            throw new NoPostsFoundException("게시글이 없습니다.");
        }
        return PageResponseDto.<PostResDto>withAll()
                .dtoList(posts)
                .pageRequestDto(pageRequestDto)
                .total(postPage.getTotalElements())
                .build();
    }



    @Override
    public PageResponseDto<PostResDto> findAll(Long memberId, String title, PageRequestDto pageRequestDto) {
        PageRequest pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());

        // 제목으로 검색해서 page로 list return
        Page<Post> searchPage = postRepository.findByTitleContaining(title, pageable);
        List<PostResDto> postList = searchPage.getContent().stream()
                .map(entity -> {
                    PostResDto dto = PostResDto.entityToResDto(entity);
                    return dto;
                })
                .collect(Collectors.toList());
        System.out.println("PostList"+postList);
        // PageResponseDto에 withSearch() 메서드를 사용하여 반환
        return PageResponseDto.<PostResDto>withSearchPost(postList, pageRequestDto, searchPage.getTotalElements());
    }
}
