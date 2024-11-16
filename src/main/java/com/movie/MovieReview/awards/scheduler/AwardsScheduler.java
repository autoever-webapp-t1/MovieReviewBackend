package com.movie.MovieReview.awards.scheduler;

import com.movie.MovieReview.awards.service.AwardsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwardsScheduler {
    private final AwardsService awardsService;

    //매주 월요일 자정 12시에 갱신해줌
    @Scheduled(cron = "0 0 0 * * MON")
    public void updateAwardsStatus() {
        awardsService.changeStatus();
    }
}
