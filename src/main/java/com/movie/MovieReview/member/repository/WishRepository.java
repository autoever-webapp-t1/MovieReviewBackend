package com.movie.MovieReview.member.repository;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.WishEntity;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {
    // 해당 회원과 영화에 대한 위시리스트 항목을 찾는 메서드
    WishEntity findByMemberAndMovie(MemberEntity member, MovieDetailEntity movie);

    // 특정 회원의 모든 위시리스트 항목을 찾는 메서드
    List<WishEntity> findByMember(MemberEntity member);
}
