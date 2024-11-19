package com.movie.MovieReview.Heart.controller;

import com.movie.MovieReview.Heart.dto.HeartRequestDto;
import com.movie.MovieReview.Heart.service.HeartService;
import com.movie.global.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/api")
@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HeartController {
    private final HeartService heartService;
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<MessageDto> insert(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long postId, @RequestBody HeartRequestDto heartRequestDto) throws Exception {
        heartService.insert(authorizationHeader, postId, heartRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(MessageDto.msg("insert success"));
    }
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<MessageDto> delete(@RequestHeader("Authorization") String authorizationHeader, @PathVariable Long postId, @RequestBody HeartRequestDto heartRequestDto) throws Exception {
        heartService.delete(authorizationHeader, postId, heartRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(MessageDto.msg("delete success"));
    }
}