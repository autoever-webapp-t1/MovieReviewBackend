package com.movie.MovieReview.member.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.entity.WishEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.member.repository.WishRepository;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WishServiceImpl implements WishService{
    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Override
    public void addToWishlist(Long memberId, Long movieId) {
        // 회원과 영화 정보를 가져옴
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        MovieDetailEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // 이미 위시리스트에 해당 영화가 추가되어 있는지 확인
        WishEntity existingWish = wishRepository.findByMemberAndMovie(member, movie);

        if (existingWish == null) {
            // 위시리스트에 없다면 추가
            WishEntity wish = WishEntity.builder()
                    .member(member)
                    .movie(movie)
                    .build();
            wishRepository.save(wish);
        } else {
            // 이미 있다면 삭제할 수도 있음 (토글형태로 처리)
            wishRepository.delete(existingWish);
        }
    }

    @Override
    public List<MovieCardDto> getWishlistByMemberId(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        return wishRepository.findByMember(member).stream()
                .map(wish -> {
                    MovieDetailEntity movie = wish.getMovie();

                    return MovieCardDto.builder()
                            .id(movie.getId())  // Movie ID
                            .title(movie.getTitle())  // 영화 제목
                            .overview(movie.getOverview())  // 줄거리
                            .poster_path(movie.getImages())  // 포스터 URL
                            .release_date(movie.getRelease_date())  // 개봉 날짜
                            //.genre_ids(movie.getGenreIds())  // 장르 ID 목록
                            .build();
                })
                .collect(Collectors.toList());
    }


}
