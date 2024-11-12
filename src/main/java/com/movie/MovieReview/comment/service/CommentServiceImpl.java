package com.movie.MovieReview.comment.service;

import com.movie.MovieReview.comment.dao.CommentRepository;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;

import java.util.List;

public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;

    @Override
    public void deleteComment(Long commentId) {
        
    }

    @Override
    public CommentResDto addComment(Long postId, CommentReqDto commentReqDto) {
        return null;
    }

    @Override
    public CommentResDto updateComment(Long postId, CommentReqDto commentReqDto) {
        return null;
    }

    @Override
    public List<CommentResDto> findCommentPyPostId(Long postId) {
        return List.of();
    }

    @Override
    public CommentResDto findOne(Long commentId) {
        return null;
    }
}
