package com.movie.MovieReview.awards.service;

import com.movie.MovieReview.awards.dto.AwardsDto;
import com.movie.MovieReview.awards.dto.AwardsPastListDto;
import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AwardsService {
    public List<MovieDetailsDto> getNominatedMoviesDetails() throws Exception;

//    public void changeStatus();

    public List<AwardsEntity> getPastAwards();

    public AwardsDto updateAwardsStatusAndTopMovie(); //return 일등 영화 계산 후 movieId
    public  List<AwardsPastListDto> getPastAwardsDetails(); //과거 어워즈 모든 기록 제공
    public AwardsDto getCurrentAwards();

}
