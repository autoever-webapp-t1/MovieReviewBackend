package com.movie.MovieReview.post.service;

import com.movie.MovieReview.post.entitiy.Post;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    Post findByPostId(Long postId);
}
