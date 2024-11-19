package com.movie.MovieReview.post.controller;

import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.post.dto.PostDto;
import com.movie.MovieReview.post.dto.PostResDto;
import com.movie.MovieReview.post.service.PostServiceImpl;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.global.dto.MessageDto;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostServiceImpl postService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/post")
    public ResponseEntity<PostResDto> create(@RequestHeader("Authorization") String authorizationHeader, @RequestBody PostDto postDto) throws Exception {
        PostResDto createDto = postService.createPost(authorizationHeader, postDto);
        return ResponseEntity.status(HttpStatus.OK).body(createDto);
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResDto> update(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long postId, @RequestBody PostDto postDto) throws Exception {
        PostResDto updatedDto = postService.updatePost(postId, postDto);
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



    @GetMapping("/post/search/{keyword}") //키워드 포함되어 있는 거 모두 검색 with 페이지네이션
    public ResponseEntity<?> getKeywordResult(
            @PathVariable("keyword") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // 헤더에서 JWT 토큰 추출
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("클라이언트에서 헤더 토큰 오류!!!!!");
            }

            String token = authorizationHeader.substring(7); // JWT 토큰 뽑아내기

            // 토큰 유효성 검사
            if (!jwtTokenService.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰!!!!");
            }

            // 토큰에서 memberId 추출
            Long memberId = Long.valueOf(jwtTokenService.getPayload(token));


            PageRequestDto pageRequestDto = PageRequestDto.builder()
                    .page(page)
                    .size(size)
                    .build();
            PageResponseDto<PostResDto> response = postService.findAll(memberId, keyword, pageRequestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
