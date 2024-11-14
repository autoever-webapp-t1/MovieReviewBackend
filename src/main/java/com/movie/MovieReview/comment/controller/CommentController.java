package com.movie.MovieReview.comment.controller;

import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import com.movie.MovieReview.comment.service.CommentService;
import com.movie.global.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResDto> create(@PathVariable Long postId, @RequestBody CommentReqDto commentReqDto) {
        CommentResDto createDto = commentService.addComment(postId, commentReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(createDto);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResDto> update(@PathVariable Long commentId, @RequestBody CommentResDto commentResDto) {
        CommentResDto updatedDto = commentService.updateComment(commentId,commentResDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<MessageDto> delete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageDto.msg("comment delete success"));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResDto>> findAllCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.findCommentPyPostId(postId));
    }
}
