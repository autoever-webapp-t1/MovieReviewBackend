package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.service.MemberService;
import com.movie.MovieReview.member.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/info")
    public MemberDto info(){
        final long memberId = SecurityUtil.getCurrentUserId();
        MemberDto memberDto = memberService.findById(memberId);
        if(memberDto == null){
            throw new CustomException(ErrorCode.NOT_EXIST_USER);
        }
        return memberDto;
    }

    @PutMapping("/{memberId}/nickname")
    public ResponseEntity<?> updateNickname(
            @PathVariable("memberId") Long memberId,
            @RequestParam String newname) {
        try {
            memberService.updateNickname(memberId, newname);
            return ResponseEntity.ok("Nickname updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

}
