package com.movie.MovieReview.post.controller;

import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entity.Post;
import com.movie.MovieReview.post.service.PostServiceImpl;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.global.dto.MessageDto;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/post")
    public ResponseEntity<PostResDto> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PostDto postDto) throws Exception {
        PostResDto createDto = postService.createPost(authorizationHeader, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(createDto);
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResDto> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long postId, @RequestBody PostDto postDto) throws Exception {
        PostResDto updatedDto = postService.updatePost(authorizationHeader, postId, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<MessageDto> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long postId) throws Exception {
        postService.deletePost(authorizationHeader, postId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageDto.msg("delete success"));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResDto> getPost(@PathVariable Long postId) throws Exception {
        PostResDto postResDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postResDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageRequestDto pageRequestDto = PageRequestDto.builder()
                    .page(page)
                    .size(size)
                    .build();
            PageResponseDto<PostResDto> response = postService.getAllPosts(pageRequestDto);
            if (response==null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("게시글이 없습니다.");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 에러가 발생했습니다 : " +e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getFiltered(@QuerydslPredicate(root = Post.class) Predicate predicate, Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(predicate, pageable));
    }

}
