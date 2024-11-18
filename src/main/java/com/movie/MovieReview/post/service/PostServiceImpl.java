package com.movie.MovieReview.post.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.UserPrincipal;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.post.dto.PostDetailDto;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.post.exception.NoPostsFoundException;
import com.movie.MovieReview.post.exception.PostNotFoundException;
import com.movie.MovieReview.post.repository.PagingAndSortingRepository;
import com.movie.MovieReview.post.repository.PostRepository;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    PagingAndSortingRepository pagingAndSortingRepository;
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

    private MemberEntity getLoginMember(String authorizationHeader) throws Exception {
        Long memberId = extractMemberId(authorizationHeader);
        return memberRepository.findById(memberId)
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
    public PostResDto createPost(String authorizationHeader, PostDto postDto) throws Exception {
        MemberEntity member = getLoginMember(authorizationHeader);
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
    public void deletePost(String authorizationHeader, Long postId) throws Exception {
        MemberEntity member = getLoginMember(authorizationHeader);
        Post post = postRepository.findById(postId).orElseThrow(()->new PostNotFoundException());
        if (!post.getWriter().equals(member)) {
            throw new RuntimeException("접근 권한이 없습니다");
        }
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public PostResDto updatePost(String authorizationHeader, Long postId, PostResDto postDto) throws Exception {
        MemberEntity member = getLoginMember(authorizationHeader);
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
        Page<Post> postPage =  postRepository.findAll(pageable);
        List<PostDetailDto> posts = postPage.getContent().stream()
                .map(this::postDetailDto)
                .collect(Collectors.toList());
        if (posts.isEmpty()) {
            throw new NoPostsFoundException("게시글이 없습니다.");
        }
        return PageResponseDto.<PostDetailDto>withAll()
                .dtoList(posts)
                .pageRequestDto(pageRequestDto)
                .total(postPage.getTotalElements())
                .build();
    }

    @Override
    public Page<Post> findAll(Predicate predicate, Pageable pageable) {
        return pagingAndSortingRepository.findAll(predicate, pageable);
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

}
