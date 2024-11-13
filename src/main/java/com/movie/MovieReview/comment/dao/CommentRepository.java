package com.movie.MovieReview.comment.dao;

import com.movie.MovieReview.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c FROM Comment c WHERE c.post.postId = :postId")
    List<Comment> findByPost_PostId(Long postId);

    Optional<Comment> findById(Long CommentId);

    @Query("SELECT COUNT(c) from Comment c where c.post.postId=:id")
    Integer countComment(@Param("id") Long postId);

}
