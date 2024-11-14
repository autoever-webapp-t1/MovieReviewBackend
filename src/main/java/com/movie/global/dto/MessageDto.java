package com.movie.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {
    public String msg;
    public static MessageDto msg(String msg) {
        return MessageDto.builder().msg(msg).build();
    }
}
