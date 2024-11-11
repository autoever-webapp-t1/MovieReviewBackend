//package com.movie.MovieReview.comment.dto;
//
//import com.movie.MovieReview.comment.domain.Comment;
//import lombok.Builder;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//@Builder
//public class CommentResDto {
//    private final Long postId;
//    private final Long commentId;
//    private final String writerNickname;
//    private final String profile;
//    private final String content;
//    private final LocalDate createdAt;
//    private final LocalDate modifiedAt;
//
//    public static CommentResDto entityToResDto(Comment comment) {
//        return CommentResDto.builder()
//                .commentId(comment.getCommentId())
//                .postId(comment.getPost().getPostId())
////                .writerNickname(comment.getWriter().getNickName())
////                .profile(comment.getWriter().getProfile())
//                .content(comment.getContent())
//                .createdAt(comment.getCreatedDate().toLocalDate())
//                .modifiedAt(comment.getModifiedDate().toLocalDate())
//                .build();
//    }
//}
