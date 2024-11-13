package com.movie.MovieReview.post.service;

import com.movie.MovieReview.post.entitiy.Post;
import com.movie.MovieReview.post.repository.PostRepository;

public class PostServiceImpl implements PostService{
    PostRepository postRepository;
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
}
