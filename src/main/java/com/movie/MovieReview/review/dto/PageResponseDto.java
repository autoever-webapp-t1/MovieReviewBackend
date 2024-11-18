package com.movie.MovieReview.review.dto;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDto<E> {
    private List<E> dtoList;  // <E>로 제네릭을 사용
    private List<Integer> pageNumList;
    private PageRequestDto pageRequestDto;
    private boolean prev, next;
    private int totalCount, prevPage, nextPage, totalPage, current;

    @Builder(builderMethodName = "withAll")
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, long total) {
        this.dtoList = dtoList;
        this.pageRequestDto = pageRequestDto;
        this.totalCount = (int) total;

        // 페이지네이션 계산
        int size = pageRequestDto.getSize(); // 페이지 크기
        int page = pageRequestDto.getPage(); // 현재 페이지
        this.totalPage = (int) Math.ceil((double) total / size); // 전체 페이지 수

        // 페이지 번호 계산
        int start = Math.max(1, ((page - 1) / 10) * 10 + 1); // 시작 페이지 번호
        int end = Math.min(start + 9, totalPage); // 끝 페이지 번호

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        // 이전, 다음 페이지 버튼 활성화 여부
        this.prev = page > 1; // page가 1보다 크면 prev 버튼 활성화
        this.next = page < totalPage; // page가 totalPage보다 작으면 next 버튼 활성화


        // 이전, 다음 페이지 번호 설정
        this.prevPage = this.prev ? page - 1 : 0; // prev는 현재 페이지 - 1
        this.nextPage = this.next ? page + 1 : page + 1; // next는 현재 페이지 + 1

        // 현재 페이지
        this.current = page;

        // 디버깅 로그
        System.out.println("Start: " + start + ", End: " + end);
        System.out.println("Prev: " + this.prev + ", Next: " + this.next);
        System.out.println("PrevPage: " + this.prevPage + ", NextPage: " + this.nextPage);
    }

    // MovieCardDto 전용 빌더 메서드
    public static PageResponseDto<MovieCardDto> withSearch(List<MovieCardDto> dtoList, PageRequestDto pageRequestDto, long total) {
        return new PageResponseDto<>(dtoList, pageRequestDto, total);
    }
}
