//package com.movie.MovieReview.awards.scheduler;
//
//import com.movie.MovieReview.awards.service.AwardsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AwardsScheduler {
//    private final AwardsService awardsService;
//
//    //서버 키자마자 2 -> 1, 1 -> 0
//    @Scheduled(fixedRate = 600000000) // 1분 마다 실행
//    public void updateAwardsStatus() {
//        awardsService.changeStatus();
//    }
//}
