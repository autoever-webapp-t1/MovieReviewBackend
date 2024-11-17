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
    //@Scheduled(cron = "0 0 0 * * MON")
    //서버 키자마자 2 -> 1, 1 -> 0
    //@Scheduled(cron = "0 0 0 * * MON")
    //@Scheduled(fixedRate = 5000) // for test
    public void updateAwardsStatus() {
//        awardsService.changeStatus();
        System.out.println("Awards 상태 및 TopMovie 업데이트 시작");
        awardsService.updateAwardsStatusAndTopMovie();
        System.out.println("Awards 상태 및 TopMovie 업데이트 완료");
    }
}
