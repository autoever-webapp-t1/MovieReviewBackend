package com.movie.MovieReview.comment.dto;
import com.movie.MovieReview.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import java.time.LocalDateTime;
@Data
@Builder
public class CommentResDto {
    private Long commentId;
    private Long postId;
    private String nickname;
    private String content;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String profile;
    public static CommentResDto entityToResDto(Comment comment) {
        return CommentResDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .nickname(comment.getWriter().getNickname())
                .content(comment.getContent())
                .memberId(comment.getWriter().getMemberId())
                .createdAt(comment.getCreatedDate())
                .updatedAt(comment.getModifiedDate())
                .build();
    }
}