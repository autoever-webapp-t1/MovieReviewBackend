package com.movie.MovieReview.awards.service;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AwardsService {
    public List<MovieDetailsDto> getNominatedMoviesDetails(Long awardId) throws Exception;

    public void changeStatus();

    public List<AwardsEntity> getPastAwards();
}
