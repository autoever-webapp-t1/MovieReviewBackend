package com.movie.MovieReview.Heart.dao;
import com.movie.MovieReview.Heart.entity.Heart;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndPost(MemberEntity member, Post post);
}