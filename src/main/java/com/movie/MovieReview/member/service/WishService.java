package com.movie.MovieReview.member.service;

import com.movie.MovieReview.movie.dto.MovieCardDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WishService {
    public void addToWishlist(Long memberId, Long movieId);
    public List<MovieCardDto> getWishlistByMemberId(Long memberId);
}
