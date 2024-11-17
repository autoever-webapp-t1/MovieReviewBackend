package com.movie.MovieReview.awards.service;

import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.awards.repository.AwardsRepository;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwardsServiceImpl implements AwardsService{
    private final AwardsRepository awardsRepository;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final ReviewService reviewService;

    @Override
    public List<MovieDetailsDto> getNominatedMoviesDetails(Long awardId) throws Exception {
        AwardsEntity award = awardsRepository.findById(awardId)
                .orElseThrow(() -> new RuntimeException("Award not found"));

        // nominated1, nominated2, nominated3, nominated4 무비 아이디 가져오기
        List<Long> movieIds = Arrays.asList(award.getNominated1(), award.getNominated2(), award.getNominated3(), award.getNominated4());

        // 영화정보 가져오기
        List<MovieDetailsDto> movieDetailsList = new ArrayList<>();
        for (Long movieId : movieIds) {
            if (movieId != null) {
                MovieDetailsDto movieDetails = movieService.getTopRatedMovieDetailsInDBForAwards(movieId);
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
            log.info("AwardServiceImpl: status 1 -> 0로 변경완료!!!!!");
        }

        // 상태가 2인 항목 중 하나를 1로 변경
        List<AwardsEntity> futureAwards = awardsRepository.findByStatus(2);
        if (!futureAwards.isEmpty()) {
            AwardsEntity nextAward = futureAwards.get(0);
            nextAward.setStatus(1);
            log.info("AwardServiceImpl: status 2 -> 1로 변경완료!!!!!");
        }
    }

    @Override
    public List<AwardsEntity> getPastAwards() {
        return awardsRepository.findByStatus(0); // 과거 어워즈 가져오기
    }

    @Override
    @Transactional
    public Long updateAwardsStatusAndTopMovie() {
        // 1. status가 1인 AwardsEntity 조회
        List<AwardsEntity> activeAwards = awardsRepository.findByStatus(1);
        log.info("############################Active awards count: {}", activeAwards.size());

        Long topMovieId = null;

        for (AwardsEntity awards : activeAwards) {
            // 2. AwardsEntity에서 기간 가져오기
            LocalDateTime startDate = awards.getStartDateTime();
            LocalDateTime endDate = awards.getEndDateTime();
            log.info("############################Processing AwardsEntity ID: {}, Start Date: {}, End Date: {}",
                    awards.getAwardsId(), startDate, endDate);

            // 3. nominated 영화들의 `awardsTotalAverageSkill` 계산 및 비교
            List<Long> nominatedMovieIds = Arrays.asList(
                    awards.getNominated1(),
                    awards.getNominated2(),
                    awards.getNominated3(),
                    awards.getNominated4()
            );
            log.info("############################Nominated Movie IDs: {}", nominatedMovieIds);

            MovieDetailEntity topMovie = null;
            double highestSkill = -1;

            for (Long movieId : nominatedMovieIds) {
                // 4. 영화의 `awardsTotalAverageSkill` 계산
                Map<String, Object> avgSkills = reviewService.getAverageSkillsByMovieIdAndDateRange(movieId, startDate, endDate);
                log.info("############################Movie ID: {}, Average Skills: {}", movieId, avgSkills);

                if (!avgSkills.isEmpty()) {
                    double totalAverageSkill = (double) avgSkills.getOrDefault("totalAverageSkill", 0.0);
                    log.info("############################Movie ID: {}, Total Average Skill: {}", movieId, totalAverageSkill);

                    // 가장 높은 평균값의 영화 찾기
                    if (totalAverageSkill > highestSkill) {
                        highestSkill = totalAverageSkill;
                        topMovie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new EntityNotFoundException("############################Movie not found with id: " + movieId));
                        log.info("############################New Top Movie Found: ID: {}, Skill: {}", topMovie.getId(), highestSkill);
                    }
                }
            }

            // 5. AwardsEntity에 topMovieId 설정 및 상태 업데이트
            if (topMovie != null) {
                awards.setTopMovieId(topMovie.getId());
                topMovieId = awards.getTopMovieId();
                log.info("############################Top Movie for Awards ID: {} is Movie ID: {}", awards.getAwardsId(), topMovie.getId());
            } else {
                log.info("############################No Top Movie Found for Awards ID: {}", awards.getAwardsId());
            }

            awards.setStatus(0); // 상태를 0으로 변경
            awardsRepository.save(awards); // 변경 사항 저장
            log.info("############################AwardsEntity ID: {} status updated to 0", awards.getAwardsId());
        }
        return topMovieId;
    }


}


