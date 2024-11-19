package com.movie.MovieReview.awards.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.MovieReview.awards.dto.AwardsDto;
import com.movie.MovieReview.awards.service.AwardsService;
import com.movie.MovieReview.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AwardsScheduler {
    private final AwardsService awardsService;
    private final SseService sseService;

    @Autowired
    private ObjectMapper objectMapper;

    //매주 월요일 자정 12시에 갱신해줌
//    @Scheduled(cron = "0 0 0 * * MON")
    //서버 키자마자 2 -> 1, 1 -> 0
//    @Scheduled(fixedRate = 5000) // for test
    @Scheduled(fixedRate = 300000) // for test
    public void updateAwardsStatus() {
//        awardsService.changeStatus();
        try {
            System.out.println("Awards 상태 및 TopMovie 업데이트 시작");


            AwardsDto awardsDto = awardsService.updateAwardsStatusAndTopMovie();

            String notificationMessage = objectMapper.writeValueAsString(awardsDto);

            // SSE 알림 전송
            sseService.broadcast(notificationMessage);
            System.out.println("AwardsScheduler???????????????"+notificationMessage);
            System.out.println("Awards 상태 및 TopMovie 업데이트 완료");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("예외 발생: " + e.getMessage());
        }

    }
}
