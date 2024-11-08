package com.movie.MovieReview.post.repository;

import com.movie.MovieReview.post.entitiy.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity,Long> {
}
