package com.movie.MovieReview.sse.service;

import com.movie.MovieReview.sse.dto.MessageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@Log4j2
public class SseService {
//    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<MessageDto> messageQueue = new LinkedBlockingQueue<>();

    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected!"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        emitter.onCompletion(()->emitters.remove(memberId));
        emitter.onTimeout(()->emitters.remove(memberId));
        emitter.onError((e)->emitters.remove(memberId));

        return emitter;
    }

//    public SseEmitter subscribe() {
//
//        try {
//            emitter.send(SseEmitter.event()
//                    .name("connect")
//                    .data("Connected!"));
//        } catch (IOException e) {
//            emitter.completeWithError(e);
//        }
//        emitters.add(emitter);
//        emitter.onCompletion(()->emitters.remove(emitter));
//        emitter.onTimeout(()->emitters.remove(emitter));
//        emitter.onError((e)->emitters.remove(emitter));
//
//        return emitter;
//    }

    public void send(MessageDto messageDto) {
        try {
            for(SseEmitter emitter : emitters.values()) {
                emitter.send(SseEmitter.event().name("message").data(messageDto));
            }
        } catch (Exception e) {
        }
    }

    public void setMessage(MessageDto messageDto) {
        messageQueue.offer(messageDto);
    }

    // 게시글에 댓글 달렸을 때 알람 기능 구현
    public void sendNotification(Long memberId, String message) {
        SseEmitter emitter1 = emitters.get(memberId);
        if (emitter1 != null) {
            try {
                emitter1.send(SseEmitter.event().name("NEW_COMMENT").data(message));
            } catch (IOException e) {
                emitters.remove(memberId);
            }
        }
    }
    //어워즈 기간 종료 시 결과 알림
    public void broadcast(String message) {
        Iterator<SseEmitter> iterator = emitters.values().iterator();
        while (iterator.hasNext()) {
            SseEmitter emitter = iterator.next();
            try {
                emitter.send(SseEmitter.event().name("AWARDS_NOTIFICATION").data(message));
            } catch (IOException e) {
                iterator.remove();
            }
        }
    }

    //        @Scheduled(cron = "0 0 21 ? * MON") // At 09:00 PM, 매주 목요일 실행
    @Scheduled(cron = "0 * * * * *") // 매분 0초에 실행(테스트용)
    public void alarm() {
        MessageDto messageDto = messageQueue.poll();
        if (messageDto != null) {
            String message = messageDto.getMessage();
            log.info("message: "+ message);
            Iterator<SseEmitter> iterator = emitters.values().iterator();
            while (iterator.hasNext()) {
                SseEmitter emitter = iterator.next();
                try {
                    emitter.send(SseEmitter.event().data(message));
                } catch (IOException e) {
                    iterator.remove();
                }
            }
        }
    }



    //        @Scheduled(cron = "0 0 21 ? * MON") // At 09:00 PM, 매주 목요일 실행
//    @Scheduled(cron = "0 * * * * *") // 매분 0초에 실행(테스트용)
//    public void alarm() {
//        MessageDto messageDto = messageQueue.poll();
//        if (messageDto != null) {
//            String message = messageDto.getMessage();
//            log.info("message: "+ message);
//            Iterator<SseEmitter> iterator = emitters.iterator();
//            while (iterator.hasNext()) {
//                SseEmitter emitter = iterator.next();
//                try {
//                    emitter.send(SseEmitter.event().data(message));
//                } catch (IOException e) {
//                    iterator.remove();
//                }
//            }
//        }
//    }
}


