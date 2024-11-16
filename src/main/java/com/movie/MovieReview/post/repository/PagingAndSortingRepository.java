package com.movie.MovieReview.post.repository;
import com.movie.MovieReview.post.dto.PostDetailDto;
import com.movie.MovieReview.post.entitiy.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PagingAndSortingRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);
}
