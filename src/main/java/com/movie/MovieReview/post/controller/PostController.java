package com.movie.MovieReview.post.controller;

import com.movie.MovieReview.post.dto.PostDetailDto;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.entitiy.Post;
import com.movie.MovieReview.post.service.PostServiceImpl;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.global.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PostController {
    private final PostServiceImpl postService;
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<PostResDto> create(@RequestBody PostDto postDto) {
        PostResDto createDto = postService.createPost(postDto);
        return ResponseEntity.status(HttpStatus.OK).body(createDto);
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResDto> update(@PathVariable Long postId, @RequestBody PostResDto postResDto) {
        PostResDto updatedDto = postService.updatePost(postId, postResDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<MessageDto> delete(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(MessageDto.msg("delete success"));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDetailDto> getPost(@PathVariable Long postId) {
        PostDetailDto postDetailDto = postService.getPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postDetailDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<PageResponseDto<PostDetailDto>> getAllPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequestDto pageRequestDto = PageRequestDto.builder()
                .page(page)
                .size(size)
                .build();
        PageResponseDto<PostDetailDto> response = postService.getAllPosts(pageRequestDto);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/search")
//    public ResponseEntity searchTitle(@RequestParam(value = "title", required = false) String title,
//                                      @RequestParam int page,
//                                      @RequestParam int size) {
//        Page<Post> pagePosts = postService.searchByTitle(title, page, size);
//        List<Post> posts = pagePosts.getContent();
//        return new ResponseEntity<>(new MultiResponseDto<>(mapper.postToPostResDtos(posts),pagePosts),HttpStatus.OK);
//    }

}
