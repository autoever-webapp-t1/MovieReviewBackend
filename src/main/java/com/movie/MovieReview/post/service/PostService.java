package com.movie.MovieReview.post.service;

import com.movie.MovieReview.post.dto.PostDetailDto;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    Post findByPostId(Long postId);
    PostResDto createPost(PostDto postDto);
    void deletePost(Long postId);
    PostResDto updatePost(Long postId, PostResDto postResDto);
    List<PostResDto> findPostByMemberId(Long memberId);
    PostDetailDto getPost(Long postId);
    PageResponseDto<PostDetailDto> getAllPosts(PageRequestDto pageRequestDto);
//    Page<Post> searchByTitle(String title, int page, int size);
}
