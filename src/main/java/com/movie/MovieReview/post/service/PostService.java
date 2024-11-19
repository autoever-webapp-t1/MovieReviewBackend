package com.movie.MovieReview.post.service;

import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface PostService {
    Post findByPostId(Long postId);
    PostResDto createPost(String authorizationHeader, PostDto postDto) throws Exception;
    void deletePost(String authorizationHeader,Long postId) throws Exception;
    PostResDto updatePost(Long postId, PostDto postDto) throws Exception;
    List<PostResDto> findPostByMemberId(Long memberId);
    PostResDto getPost(Long postId) throws Exception;
    PageResponseDto<PostResDto> getAllPosts(PageRequestDto pageRequestDto);
    PageResponseDto<PostResDto> findAll(Long memberId, String title, PageRequestDto pageRequestDto);
}
