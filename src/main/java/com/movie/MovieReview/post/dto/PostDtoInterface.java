package com.movie.MovieReview.post.dto;
import java.time.LocalDateTime;
public interface PostDtoInterface {
    Long getPostId();
    String title();
    String content();
    boolean isLiked();
    Integer likesCount();
    LocalDateTime createdDate();
    LocalDateTime modifiedDate();
}