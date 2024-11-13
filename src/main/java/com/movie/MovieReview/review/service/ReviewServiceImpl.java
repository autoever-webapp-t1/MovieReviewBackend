package com.movie.MovieReview.review.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.review.dto.MyReviewsDto;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.MovieReview.review.entity.ReviewEntity;
import com.movie.MovieReview.review.repository.ReviewRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    @Transactional
    public Long createReview(ReviewDetailDto dto) {
        Long memberId = dto.getMemberId();
        Long movieId = dto.getMovieId();

        if (memberId == null) {
            throw new IllegalArgumentException("Member ID must not be null");
        }

        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID must not be null");
        }

        Optional<MemberEntity> member = memberRepository.findById(memberId);
        MemberEntity foundMember = member.orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Optional<MovieDetailEntity> movie = movieRepository.findById(movieId);
        MovieDetailEntity foundMovie = movie.orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        ReviewEntity reviewEntity = toEntity(dto, foundMember,foundMovie);

        ReviewEntity result = reviewRepository.save(reviewEntity);
        log.info("result: " + result.getMember().getMemberId() );
        return result.getReviewId();
    }

    @Override
    @Transactional
    public void modifyReview(ReviewDetailDto dto) {
        Long memberId = dto.getMemberId();
        Long movieId = dto.getMovieId();
        Long reviewId = dto.getReviewId();

        if (memberId == null) {
            throw new IllegalArgumentException("Member ID must not be null");
        }

        if (movieId == null) {
            throw new IllegalArgumentException("Movie ID must not be null");
        }

        if (reviewId == null) {
            throw new IllegalArgumentException("Review ID must not be null");
        }

        // 멤버와 영화가 존재하는지 확인
        MemberEntity foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    // 멤버가 존재하지 않으면 오류를 발생시킴
                    System.out.println("Member not found for ID: " + memberId);  // 로그 추가
                    return new IllegalArgumentException("Member not found");
                });
        MovieDetailEntity foundMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        // 수정할 리뷰가 존재하는지 확인
        ReviewEntity existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        // 기존 리뷰 내용 수정
        existingReview.setContent(dto.getContent());
        existingReview.setTotalHeart(dto.getTotalHeart());
        existingReview.setMyHeart(dto.isMyHeart());
        existingReview.setActorSkill(dto.getActorSkill());
        existingReview.setDirectorSkill(dto.getDirectorSkill());
        existingReview.setSceneSkill(dto.getSceneSkill());
        existingReview.setMusicSkill(dto.getMusicSkill());
        existingReview.setStorySkill(dto.getStorySkill());
        existingReview.setLineSkill(dto.getLineSkill());

        // 변경 사항 저장
        reviewRepository.save(existingReview);
    }

    @Override
    public void removeReview(Long movieId, Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        if (!reviewEntity.getMovie().getId().equals(movieId)) {
            throw new IllegalArgumentException("Review does not belong to the specified movie");
        }

        reviewRepository.delete(reviewEntity);
    }

    @Override
    public ReviewDetailDto getReview(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        return toDto(reviewEntity);
    }

    @Override
    public List<ReviewDetailDto> getAllReviews() {
        List<ReviewEntity> reviews = reviewRepository.findAll();
        return reviews.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void toggleLike(Long reviewId) {
        ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        reviewEntity.setMyHeart(!reviewEntity.isMyHeart());  // Toggle the like status
        reviewRepository.save(reviewEntity);
    }

    @Override
    public Map<String, Object> getAverageSkillsByMemberId(Long memberId) {
        return reviewRepository.findAverageSkillsByMemberId(memberId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MyReviewsDto> getMemberReviews(Long memberId) {
        // 해당 회원의 모든 리뷰 엔티티 가져오기
        List<ReviewEntity> reviewEntities = reviewRepository.findAllReviewsByMemberId(memberId);

        // ReviewEntity 리스트를 MyReviewsDto 리스트로 변환
        return reviewEntities.stream()
                .map(this::toMyPageDto)
                .collect(Collectors.toList());
    }

    public ReviewDetailDto toDto(ReviewEntity reviewEntity){
        double avgSkill = Math.round(
                ((double)(reviewEntity.getActorSkill() + reviewEntity.getDirectorSkill() +
                        reviewEntity.getSceneSkill() + reviewEntity.getMusicSkill() +
                        reviewEntity.getStorySkill() + reviewEntity.getLineSkill()) / 12) * 100
        ) / 100.0;

        return ReviewDetailDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .nickname(reviewEntity.getMember().getNickname())
                .profile(reviewEntity.getMember().getProfile())
                .memberId(reviewEntity.getMember().getMemberId())
                .movieId(reviewEntity.getMovie().getId())
                .title(reviewEntity.getMovie().getTitle())
                .content(reviewEntity.getContent())
                .createdDate(reviewEntity.getCreatedDate())
                .modifyDate(reviewEntity.getModifiedDate())
                .totalHeart(reviewEntity.getTotalHeart())
                .myHeart(reviewEntity.isMyHeart())
                .actorSkill(reviewEntity.getActorSkill())
                .directorSkill(reviewEntity.getDirectorSkill())
                .sceneSkill(reviewEntity.getSceneSkill())
                .musicSkill(reviewEntity.getMusicSkill())
                .storySkill(reviewEntity.getStorySkill())
                .lineSkill(reviewEntity.getLineSkill())
                .avgSkill(avgSkill)
                .build();
    }

    public MyReviewsDto toMyPageDto(ReviewEntity reviewEntity){

        double avgSkill = Math.round(
                ((double)(reviewEntity.getActorSkill() + reviewEntity.getDirectorSkill() +
                        reviewEntity.getSceneSkill() + reviewEntity.getMusicSkill() +
                        reviewEntity.getStorySkill() + reviewEntity.getLineSkill()) / 12) * 100
        ) / 100.0;
        return MyReviewsDto.builder()
                .reviewId(reviewEntity.getReviewId())
                .movieId(reviewEntity.getMovie().getId())
                .title(reviewEntity.getMovie().getTitle())
                .images(reviewEntity.getMovie().getImages())
                .content(reviewEntity.getContent())
                .createdDate(reviewEntity.getCreatedDate())
                .modifyDate(reviewEntity.getModifiedDate())
                .totalHeart(reviewEntity.getTotalHeart())
                .myHeart(reviewEntity.isMyHeart())
                .actorSkill(reviewEntity.getActorSkill())
                .directorSkill(reviewEntity.getDirectorSkill())
                .sceneSkill(reviewEntity.getSceneSkill())
                .musicSkill(reviewEntity.getMusicSkill())
                .storySkill(reviewEntity.getStorySkill())
                .lineSkill(reviewEntity.getLineSkill())
                .avgSkill(avgSkill)
                .build();
    }

    public ReviewEntity toEntity(ReviewDetailDto reviewDetailDto, MemberEntity member, MovieDetailEntity movie){

        return ReviewEntity.builder()
                .reviewId(reviewDetailDto.getReviewId())
                .movie(movie)
                .member(member)
                .content(reviewDetailDto.getContent())
                .totalHeart(reviewDetailDto.getTotalHeart())
                .myHeart(reviewDetailDto.isMyHeart())
                .actorSkill(reviewDetailDto.getActorSkill())
                .directorSkill(reviewDetailDto.getDirectorSkill())
                .sceneSkill(reviewDetailDto.getSceneSkill())
                .musicSkill(reviewDetailDto.getMusicSkill())
                .storySkill(reviewDetailDto.getStorySkill())
                .lineSkill(reviewDetailDto.getLineSkill())
                .build();
    }
}