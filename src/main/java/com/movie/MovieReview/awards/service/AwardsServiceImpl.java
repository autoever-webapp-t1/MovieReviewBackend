package com.movie.MovieReview.awards.service;

import com.movie.MovieReview.awards.dto.AwardsDto;
import com.movie.MovieReview.awards.dto.AwardsMovieCardDto;
import com.movie.MovieReview.awards.dto.AwardsPastListDto;
import com.movie.MovieReview.awards.entity.AwardsEntity;
import com.movie.MovieReview.awards.repository.AwardsRepository;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.review.repository.ReviewRepository;
import com.movie.MovieReview.review.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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

//    @Transactional
//    @Override
//    public void changeStatus(){
//        List<AwardsEntity> currentAwards = awardsRepository.findByStatus(1);
//        for (AwardsEntity award : currentAwards) {
//            award.setStatus(0);
//            log.info("AwardServiceImpl: status 1 -> 0로 변경완료!!!!!");
//        }
//
//        // 상태가 2인 항목 중 하나를 1로 변경
//        List<AwardsEntity> futureAwards = awardsRepository.findByStatus(2);
//        if (!futureAwards.isEmpty()) {
//            AwardsEntity nextAward = futureAwards.get(0);
//            nextAward.setStatus(1);
//            log.info("AwardServiceImpl: status 2 -> 1로 변경완료!!!!!");
//        }
//    }

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
        // 상태가 2인 항목 중 하나를 1로 변경
        List<AwardsEntity> futureAwards = awardsRepository.findByStatus(2);
        if (!futureAwards.isEmpty()) {
            AwardsEntity nextAward = futureAwards.get(0);
            nextAward.setStatus(1);
            awardsRepository.save(nextAward);
            log.info("############################Award ID: {} status updated from 2 to 1", nextAward.getAwardsId());
        }
        return topMovieId;
    }

    // 과거 어워즈 기록 조회
    @Override
    @Transactional
    public List<AwardsPastListDto> getPastAwardsDetails() {
        List<AwardsEntity> pastAwards = awardsRepository.findByStatus(0);

        return pastAwards.stream().map(award -> {
            List<Long> nominatedMovies = List.of(
                    award.getNominated1(),
                    award.getNominated2(),
                    award.getNominated3(),
                    award.getNominated4()
            );

            List<AwardsMovieCardDto> movieCardDtos = nominatedMovies.stream()
                    .map(movieId -> {
                        MovieDetailEntity movie = movieRepository.findById(movieId)
                                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));

                        // 기본 score 값을 HashMap으로 초기화
                        Map<String, Object> score = new HashMap<>();
                        score.put("avgActorSkill", 0.0);
                        score.put("avgDirectorSkill", 0.0);
                        score.put("avgLineSkill", 0.0);
                        score.put("avgMusicSkill", 0.0);
                        score.put("avgSceneSkill", 0.0);
                        score.put("avgStorySkill", 0.0);
                        score.put("totalAverageSkill", 0.0);

                        try {
                            Map<String, Object> reviewScore = reviewService.getAverageSkillsByMovieIdAndDateRange(
                                    movieId, award.getStartDateTime(), award.getEndDateTime()
                            );
                            if (reviewScore != null) {
                                score.putAll(reviewScore); // 기존 score에 reviewScore 갱신
                            } else {
                                log.warn("Score returned by reviewService is null for movieId: {}", movieId);
                                score.put("avgActorSkill", 0.0);
                                score.put("avgDirectorSkill", 0.0);
                                score.put("avgLineSkill", 0.0);
                                score.put("avgMusicSkill", 0.0);
                                score.put("avgSceneSkill", 0.0);
                                score.put("avgStorySkill", 0.0);
                            }
                        } catch (Exception e) {
                            log.warn("Review data not found for movie ID: {}", movie.getId(), e);
//                            score = Map.of("avgActorSkill", 0.0, "avgDirectorSkill", 0.0, "avgLineSkill", 0.0, "avgMusicSkill", 0.0, "avgSceneSkill", 0.0, "avgStorySkill", 0.0, "totalAverageSkill", 0.0);
                            score.put("avgActorSkill", 0.0);
                            score.put("avgDirectorSkill", 0.0);
                            score.put("avgLineSkill", 0.0);
                            score.put("avgMusicSkill", 0.0);
                            score.put("avgSceneSkill", 0.0);
                            score.put("avgStorySkill", 0.0);
                        }


                        return AwardsMovieCardDto.builder()
                                .movieId(movieId)
                                .movieTitle(movie.getTitle())
                                .moviePoster(movie.getImages())
                                .score(score)
                                .build();
                    })
                    .toList();

            return AwardsPastListDto.builder()
                    .awardsId(award.getAwardsId())
                    .awardsName(award.getAwardName())
                    .nominatedMovies(movieCardDtos)
                    .build();
        }).toList();
    }

    @Override
    public AwardsDto getCurrentAwards(){
        List<AwardsEntity> awards = awardsRepository.findByStatus(1);

        if (awards.isEmpty()) {
            throw new RuntimeException("No awards found with status 1");
        }

        //현재 진행중인 어워즈 가져오기
        AwardsEntity awardEntity = awards.get(0);

        AwardsDto awardsDto = AwardsDto.builder()
                .awardsId(awardEntity.getAwardsId())
                .awardName(awardEntity.getAwardName())
                .nominated1(awardEntity.getNominated1())
                .nominated2(awardEntity.getNominated2())
                .nominated3(awardEntity.getNominated3())
                .nominated4(awardEntity.getNominated4())
                .startDateTime(awardEntity.getStartDateTime())
                .endDateTime(awardEntity.getEndDateTime())
                .build();

        return awardsDto;
    }

//    @Override
//    public AwardsDto getAwardsByTopMovieId(Long topMovieId) {
//        // topMovieId로 AwardsEntity 찾기
//        Optional<AwardsEntity> awardsEntity = awardsRepository.findByTopMovieId(topMovieId);
//
//        // 찾은 AwardsEntity를 AwardsDto로 변환하여 반환
//        return awardsEntity.map(entity -> AwardsDto.builder()
//                        .awardName(entity.getAwardName())
//                        .nominated1(entity.getNominated1())
//                        .nominated2(entity.getNominated2())
//                        .nominated3(entity.getNominated3())
//                        .nominated4(entity.getNominated4())
//                        .startDateTime(entity.getStartDateTime())
//                        .endDateTime(entity.getEndDateTime())
//                        .topMovieId(entity.getTopMovieId())
//                        .build())
//                .orElse(null); // 없으면 null 반환
//    }

}


