package com.movie.MovieReview.comment.service;

import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import java.util.List;

public interface CommentService {
    void deleteComment(Long commentId);
    CommentResDto addComment(Long postId, CommentReqDto commentReqDto);
    CommentResDto updateComment(Long commentId, CommentResDto commentResDto);
    List<CommentResDto> findCommentByPostId(Long postId);
    Integer countComment(Long postId);
}
