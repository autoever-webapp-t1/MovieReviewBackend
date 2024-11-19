package com.movie.MovieReview.comment.controller;
import com.movie.MovieReview.comment.service.CommentService;
import com.movie.MovieReview.comment.dto.CommentReqDto;
import com.movie.MovieReview.comment.dto.CommentResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.movie.global.dto.MessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResDto> create(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long postId, @RequestBody CommentReqDto commentReqDto) throws Exception {
        CommentResDto createDto = commentService.addComment(authorizationHeader, postId, commentReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(createDto);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResDto> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long commentId, @RequestBody CommentReqDto commentReqDto) {
        CommentResDto updatedDto = commentService.updateComment(commentId,commentReqDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<MessageDto> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long commentId) throws Exception {
        commentService.deleteComment(authorizationHeader, commentId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageDto.msg("comment delete success"));
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentResDto>> findAllCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.findCommentByPostId(postId));
    }
}