package com.movie.MovieReview.member.repository;

import com.movie.MovieReview.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Optional<MemberEntity> findById(Long id);

    Optional<MemberEntity> findByRefreshToken(String refreshToken);
}
