package com.movie.MovieReview.admin.controller;

import com.movie.MovieReview.admin.dto.AwardAddMoviesDto;
import com.movie.MovieReview.admin.service.AdminServiceImpl;
import com.movie.MovieReview.awards.entity.AwardsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AwardPageController {
    private final AdminServiceImpl adminService;

    @PostMapping("/add")
    public AwardsEntity addMoviesToAward(@RequestHeader("Authorization") String authorizationHeader, @RequestBody AwardAddMoviesDto dto) throws Exception {
        AwardsEntity award = adminService.addAwardWithMovies(authorizationHeader, dto);
        return award;
    }
}
