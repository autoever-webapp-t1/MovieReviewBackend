package com.movie.MovieReview.review.service;

import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.movie.service.MovieService;
import com.movie.MovieReview.movie.service.MovieServiceImpl;
import com.movie.MovieReview.review.dto.MyReviewsDto;
import com.movie.MovieReview.review.dto.PageRequestDto;
import com.movie.MovieReview.review.dto.PageResponseDto;
import com.movie.MovieReview.review.dto.ReviewDetailDto;
import com.movie.MovieReview.review.entity.ReviewEntity;
import com.movie.MovieReview.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
    @Transactional
    public PageResponseDto<ReviewDetailDto> getAllReviewsByMovieId(Long movieId, PageRequestDto pageRequestDto) {
        // Convert PageRequestDto to Pageable
        PageRequest pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());

        // Fetch paginated reviews for the movie
        Page<ReviewEntity> reviewPage = reviewRepository.findByMovieId(movieId, pageable);

        // Convert the Page of ReviewEntity to ReviewDetailDto
        List<ReviewDetailDto> reviewList = reviewPage.getContent().stream()
                .map(this::toDto)  // Assuming ReviewDetailDto constructor that maps ReviewEntity to DTO
                .collect(Collectors.toList());

        // Create and return a PageResponseDto
        return PageResponseDto.<ReviewDetailDto>withAll()
                .dtoList(reviewList)
                .pageRequestDto(pageRequestDto)
                .total(reviewPage.getTotalElements())
                .build();
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
    @Transactional
    public Map<String, Object> getAverageSkillsByMovieId(Long movieId) {

        // 리뷰 평균 데이터를 가져오기
        Map<String, Object> avgSkills = reviewRepository.findAverageSkillsByMovieId(movieId);

        // 결과가 비어 있는 경우 빈 Map 반환
        if (avgSkills == null || avgSkills.isEmpty()) {
            return Collections.emptyMap();
        }

        // 각 평균값들을 소수점 둘째 자리까지 반올림
        avgSkills.replaceAll((key, value) ->
                value != null ? Math.round(((double) value) * 100.0) / 100.0 : 0.0
        );

        // 평균값들을 더해 전체 평균 계산
        double totalAvg = avgSkills.values().stream()
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(value -> (double) value)
                .average()
                .orElse(0.0);

        // 소수점 둘째 자리까지 반올림
        double roundedTotalAvg = Math.round(totalAvg * 100.0) / 100.0;

        // Movie 엔티티에 totalAverageSkill 업데이트
        MovieDetailEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));
        movie.setTotalAverageSkill(roundedTotalAvg);
        movieRepository.save(movie); // 변경 사항 저장

        // 결과 반환
        avgSkills.put("totalAverageSkill", roundedTotalAvg);
        return avgSkills;
    }
//    어워즈에서 이용함.
    @Override
    @Transactional
    public Map<String, Object> getAverageSkillsByMovieIdAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate) {
        // Repository에서 데이터 조회
        Map<String, Object> avgSkills = reviewRepository.findAverageSkillsByMovieIdWithinDateRange(movieId, startDate, endDate);

        // 결과가 비어 있는 경우 빈 Map 반환
        if (avgSkills == null || avgSkills.isEmpty()) {
            return Collections.emptyMap();
        }

        // 평균값들을 더해 전체 평균 계산
        double totalAvg = avgSkills.values().stream()
                .filter(Objects::nonNull) // null 값 제외
                .mapToDouble(value -> (double) value)
                .average()
                .orElse(0.0);

        // 소수점 둘째 자리까지 반올림
        double roundedTotalAvg = Math.round(totalAvg * 100.0) / 100.0;

        // Movie 엔티티의 totalAverageSkill 업데이트
        MovieDetailEntity movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));
        movie.setAwardsTotalAverageSkill(roundedTotalAvg);
        movieRepository.save(movie); // 변경 사항 저장

        avgSkills.put("totalAverageSkill", roundedTotalAvg);
        return avgSkills;
    }

//    @Override
//    @Transactional
//    public Map<String, Object> getAverageSkillsByMovieIdAndDateRange(Long movieId, LocalDateTime startDate, LocalDateTime endDate){
//        // 리뷰 평균 데이터를 가져오기
//        Map<String, Object> avgSkills = reviewRepository.findAverageSkillsByMovieIdWithinDateRange(movieId,startDate, endDate);
//
//        if (avgSkills == null || avgSkills.isEmpty()) {
//            return Collections.emptyMap();
//        }
//
////        Map<String, Object> avgSkills = new HashMap<>();
//
//        // 평균값들을 더해 전체 평균 계산
//        double totalAvg = avgSkills.values().stream()
//                .mapToDouble(value -> value != null ? ((Number) value).doubleValue() : 0.0)  // null 처리
//                .average()
//                .orElse(0.0);
//
//        // 소수점 둘째 자리까지 반올림
//        double roundedTotalAvg = Math.round(totalAvg * 100.0) / 100.0;
//
//        // Movie 엔티티에 totalAverageSkill 업데이트
//        MovieDetailEntity movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));
//        movie.setAwardsTotalAverageSkill(roundedTotalAvg);
//        movieRepository.save(movie) ; // 변경 사항 저장
//
//        // 결과 반환
//        avgSkills.put("totalAverageSkill", roundedTotalAvg);
//        return avgSkills;
//    }

    @Override
    @Transactional(readOnly = true)
    public List<MyReviewsDto> getMemberReviews(Long memberId) {

        List<ReviewEntity> reviewEntities = reviewRepository.findAllReviewsByMemberId(memberId);

        return reviewEntities.stream()
                .map(this::toMyPageDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getLatestReviewSkills(Long memberId, Long movieId) {
        // 최신 리뷰 한 건 조회
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "createdDate"));
        List<Map<String, Object>> result = reviewRepository.findTopByMemberIdAndMovieIdOrderByCreatedDateDesc(memberId, movieId, pageable);

        // 결과가 비어 있는 경우 null 반환
        if (result == null || result.isEmpty()) {
            log.warn("No review found for member ID: {} and movie ID: {}", memberId, movieId);
            return null;
        }

        Map<String, Object> mySkills = result.get(0);

        // 평균값들을 더해 전체 평균 계산
        double totalAvg = mySkills.values().stream()
                .filter(Objects::nonNull) // null 값 제외
                .mapToInt(value -> (int) value)
                .average()
                .orElse(0.0);

        // 소수점 둘째 자리까지 반올림
        double roundedTotalAvg = Math.round(totalAvg * 100.0) / 100.0;

        // avgSkill 값 추가
        mySkills.put("avgSkill", roundedTotalAvg);

        return mySkills;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovieCardDto> getMovieCardDtosByMemberId(Long memberId) {
        // 해당 회원이 작성한 모든 리뷰 조회
        List<ReviewEntity> reviews = reviewRepository.findAllReviewsByMemberId(memberId);

        List<MovieCardDto> movieCardDtos = new ArrayList<>();
        Set<Long> addedMovieIds = new HashSet<>();

        for (ReviewEntity review : reviews) {
            Long movieId = review.getMovie().getId();

            // 이미 리스트에 추가된 영화는 건너뜀
            if (addedMovieIds.contains(movieId)) {
                continue;
            }
            Map<String, Object> avgSkills = getAverageSkillsByMovieId(review.getMovie().getId());

            // score가 비어있으면 기본 값 설정
            if (avgSkills == null || avgSkills.isEmpty()) {
                avgSkills = new HashMap<>(); // 빈 Map으로 설정
            }

            // 내가 가장 최근에 작성한 리뷰의 점수 가져오기 (평균 포함)
            Map<String, Object> mySkills = getLatestReviewSkills(memberId, movieId);

            MovieCardDto movieCardDto = MovieCardDto.builder()
                    .id(review.getMovie().getId())
                    .title(review.getMovie().getTitle())
                    .overview(review.getMovie().getOverview())
                    .poster_path(review.getMovie().getImages())
                    .release_date(review.getMovie().getRelease_date())
                    .genre_ids(review.getMovie().getGenres())
                    .score(avgSkills)
                    .myScore(mySkills)
                    .build();

            movieCardDtos.add(movieCardDto);
            addedMovieIds.add(movieId);
        }

        return movieCardDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<ReviewDetailDto> getAllReviewsByMemberId(Long memberId, PageRequestDto pageRequestDto) {
        // PageRequestDto를 Pageable로 변환
        PageRequest pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize());

        // 사용자가 작성한 리뷰를 페이지네이션으로 조회
        Page<ReviewEntity> reviewPage = reviewRepository.findByMemberId(memberId, pageable);

        // Page<ReviewEntity>를 ReviewDetailDto로 변환
        List<ReviewDetailDto> reviewList = reviewPage.getContent().stream()
                .map(this::toDto)  // ReviewEntity를 ReviewDetailDto로 변환
                .collect(Collectors.toList());

        // PageResponseDto 반환
        return PageResponseDto.<ReviewDetailDto>withAll()
                .dtoList(reviewList)
                .pageRequestDto(pageRequestDto)
                .total(reviewPage.getTotalElements())
                .build();
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

    public MovieCardDto toMovieCardDto(MovieDetailEntity movieDetailEntity) {
        return MovieCardDto.builder()
                .id(movieDetailEntity.getId())
                .title(movieDetailEntity.getTitle())
                .overview(movieDetailEntity.getOverview())
                .poster_path(movieDetailEntity.getImages())
                .release_date(movieDetailEntity.getRelease_date())
                .genre_ids(movieDetailEntity.getGenres())
                .build();
    }
}