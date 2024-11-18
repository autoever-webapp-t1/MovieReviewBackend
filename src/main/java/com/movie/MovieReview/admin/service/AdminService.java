package com.movie.MovieReview.admin.service;

import com.movie.MovieReview.admin.dto.AwardAddMoviesDto;
import com.movie.MovieReview.awards.entity.AwardsEntity;

public interface AdminService {
    AwardsEntity addAwardWithMovies(AwardAddMoviesDto dto) throws Exception;
}
