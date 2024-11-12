package com.movie.MovieReview.member.controller;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.service.MemberService;
import com.movie.MovieReview.member.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
