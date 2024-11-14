package com.movie.MovieReview.review.dto;

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

        int end = (int) (Math.ceil(pageRequestDto.getPage() / 10.0)) * 10;
        int start = end - 9;

        int last = (int) (Math.ceil(totalCount / (double) (pageRequestDto.getSize())));

        end = end > last ? last : end;

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        this.prev = start > 1;
        this.next = totalCount > end * pageRequestDto.getSize();

        if (prev) {
            this.prevPage = start - 1;
        }
        if (next) {
            this.nextPage = end + 1;
        }

        this.totalPage = this.pageNumList.size();
        this.current = pageRequestDto.getPage();
    }
}