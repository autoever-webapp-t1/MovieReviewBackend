package com.movie.MovieReview.comment.service;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import java.util.List;
public interface CommentService {
    void deleteComment(String authorizationHeader, Long commentId) throws Exception;
    CommentResDto addComment(String authorizationHeader, Long postId, CommentReqDto commentReqDto) throws Exception;
    CommentResDto updateComment(Long commentId, CommentResDto commentResDto);
    List<CommentResDto> findCommentByPostId(Long postId);
    CommentResDto findOne(Long commentId);
    Integer countComment(Long postId);
}