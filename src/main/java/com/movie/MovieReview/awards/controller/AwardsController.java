package com.movie.MovieReview.awards.controller;

import com.movie.MovieReview.awards.dto.AwardsMovieCardDto;
import com.movie.MovieReview.awards.dto.AwardsPastListDto;
import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.awards.service.AwardsService;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/awards")
@CrossOrigin("*")
public class AwardsController {
    private final AwardsService awardsService;

    @GetMapping("/{awardId}") //nominated된 영화들 4개 정보 보여줌
    public List<MovieDetailsDto> getAwardMovieDetails(@PathVariable("awardId") Long awardId) {
        try {
            return awardsService.getNominatedMoviesDetails(awardId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
//
//    @GetMapping("")
//    public List<AwardsEntity> getPastAwards() {
//        List<AwardsEntity> pastAwards = awardsService.getPastAwards();
//        return pastAwards;
//    }
    @GetMapping("/past")
    public ResponseEntity<List<AwardsPastListDto>> getPastAwardsDetails() {
        List<AwardsPastListDto> pastAwardsDetails = awardsService.getPastAwardsDetails();
        return ResponseEntity.ok(pastAwardsDetails);
    }
}
