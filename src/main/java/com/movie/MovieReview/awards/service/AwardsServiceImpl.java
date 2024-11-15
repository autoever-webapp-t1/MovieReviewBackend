package com.movie.MovieReview.awards.service;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.awards.repository.AwardsRepository;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwardsServiceImpl implements AwardsService{
    private final AwardsRepository awardsRepository;
    private final MovieService movieService;

    @Override
    public List<MovieDetailsDto> getNominatedMoviesDetails(Long awardId) throws Exception {
        AwardsEntity award = awardsRepository.findById(awardId)
                .orElseThrow(() -> new RuntimeException("Award not found"));

        // nominated1, nominated2, nominated3, nominated4 값을 가져옵니다.
        List<Long> movieIds = Arrays.asList(award.getNominated1(), award.getNominated2(), award.getNominated3(), award.getNominated4());

        // 영화정보 가져오기
        List<MovieDetailsDto> movieDetailsList = new ArrayList<>();
        for (Long movieId : movieIds) {
            if (movieId != null) {
                MovieDetailsDto movieDetails = movieService.getTopRatedMovieDetailsInDB(movieId);
                if (movieDetails != null) {
                    movieDetailsList.add(movieDetails);
                }
            }
        }

        return movieDetailsList;
    }

    @Transactional
    @Override
    public void changeStatus(){
        List<AwardsEntity> currentAwards = awardsRepository.findByStatus(1);
        for (AwardsEntity award : currentAwards) {
            award.setStatus(0);
            log.info("AwardServiceImpl: status 0 -> 1로 변경완료!!!!!");
        }

        // 상태가 2인 항목 중 하나를 1로 변경
        List<AwardsEntity> futureAwards = awardsRepository.findByStatus(2);
        if (!futureAwards.isEmpty()) {
            AwardsEntity nextAward = futureAwards.get(0);
            nextAward.setStatus(1);
            log.info("AwardServiceImpl: status 1 -> 2로 변경완료!!!!!");
        }
    }

    @Override
    public List<AwardsEntity> getPastAwards() {
        return awardsRepository.findByStatus(0); // 과거 어워즈 가져오기
    }
}


