package com.movie.MovieReview.post.service;

import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface PostService {
    Post findByPostId(Long postId);
    PostResDto createPost(String authorizationHeader, PostDto postDto) throws Exception;
    void deletePost(String authorizationHeader,Long postId) throws Exception;
    PostResDto updatePost(String authorizationHeader, Long postId, PostResDto postResDto) throws Exception;
    List<PostResDto> findPostByMemberId(Long memberId);
    PostResDto getPost(String authorizationHeader, Long postId) throws Exception;
    PageResponseDto<PostResDto> getAllPosts(PageRequestDto pageRequestDto);
    Page<Post> findAll(Predicate predicate, Pageable pageable);
}
