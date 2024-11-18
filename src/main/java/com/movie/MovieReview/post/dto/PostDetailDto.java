package com.movie.MovieReview.post.dto;

import com.movie.MovieReview.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailDto{
    private Long postId;
    private Long memberId;
    private String title;
    private String content;
    private String nickname;
    private Integer likesCount;
    private boolean liked;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String profile_image;
    private int commentCnt;
}
