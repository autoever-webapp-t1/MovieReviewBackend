package com.movie.MovieReview.post.dto;

import com.movie.MovieReview.post.entity.Post;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResDto {
    private Long postId;
    private Long memberId;
    private String nickname;
    private String title;
    private String content;
    private Integer commentCnt;
    private boolean liked;
    private Integer likesCount;
    private String profileImage;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String mainImgUrl;
    private String textContent;

    public static PostResDto entityToResDto(Post post) {
        return PostResDto.builder()
                .memberId(post.getWriter().getMemberId())
                .postId(post.getPostId())
                .mainImgUrl(post.getMainImgUrl())
                .nickname(post.getWriter().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCnt(post.getCommentCnt())
                .liked(post.isLiked())
                .textContent(post.getTextContent())
                .likesCount(post.getLikesCount())
                .createdDate(post.getCreatedDate())
                .modifiedDate(post.getModifiedDate())
                .profileImage(post.getWriter().getProfile())
                .build();
    }

}
