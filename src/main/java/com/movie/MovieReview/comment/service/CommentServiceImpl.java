package com.movie.MovieReview.comment.service;

import com.movie.MovieReview.comment.dao.CommentRepository;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import com.movie.MovieReview.comment.entity.Comment;

import java.util.List;

public class CommentServiceImpl implements CommentService{
    private CommentRepository commentRepository;

    @Override
    public void deleteComment(Long commentId) {
        if (commentRepository.findById(commentId)!=null){
            commentRepository.delete(commentRepository.getReferenceById(commentId));
        }
    }

    @Override
    public CommentResDto addComment(Long postId, CommentReqDto commentReqDto) {
        // 각 포스트 하나에서 댓글을 달기
        // 해당하는 포스트에서 댓글을 여러 개 단다. 프론트단에서 내용을 입력하면
        // reqdto에 담아서 service 구현단을 호출하면 구현단에서 dto를 가지고 기능을 구현해
        // 댓글 객체를 만들어서 reposiotry에 저장함

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
