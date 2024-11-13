package com.movie.MovieReview.post.repository;

import com.movie.MovieReview.post.entitiy.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

}
