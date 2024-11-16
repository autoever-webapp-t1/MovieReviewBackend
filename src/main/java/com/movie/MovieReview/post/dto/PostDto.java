package com.movie.MovieReview.post.dto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class PostDto implements PostDtoInterface {
        private Long postId;
        private String title;
        private String content;
        private boolean liked;
        private Integer likesCount;
        public PostDto(Long postId, String title, String content) {
            this.postId = postId;
            this.title = title;
            this.content = content;
        }
        @Override
        public Long getPostId() {
            return postId;
        }
        @Override
        public String title() {
            return title;
        }
        @Override
        public String content() {
            return content;
        }
        @Override
        public boolean liked() {
            return liked;
        }
        @Override
        public Integer likesCount() {
            return likesCount;
        }
        @Override
        public LocalDateTime createdDate() {
            return null;
        }
        @Override
        public LocalDateTime modifiedDate() {
            return null;
        }
    }