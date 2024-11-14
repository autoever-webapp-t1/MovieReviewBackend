package com.movie.MovieReview.movie.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.movie.MovieReview.movie.dto.*;
import com.movie.MovieReview.movie.entity.MovieDetailEntity;
import com.movie.MovieReview.movie.entity.TopRatedMovieIdEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import com.movie.MovieReview.movie.repository.TopRatedMovieIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class MovieServiceImpl implements  MovieService{
    private final MovieRepository movieRepository;
    private final TopRatedMovieIdRepository topRatedMovieIdRepository;
    private final MovieRecommendService movieRecommendService;
    private final MovieCreditService movieCreditService;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String TMDB_API_URL = "https://api.themoviedb.org/3/movie/";
    private final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMjUxYmI1M2Q5YTNkMTA0NGRiYTcwZDFiMmI2ZGEwNSIsInN1YiI6IjY2MmNmNDRlZjZmZDE4MDEyODIyNGI3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yGcscHFGjYQq6B7s_OqCif9IH5jw8vlFboOuJZNKnTk";

    @Override
    public List<MovieCardDto> getTopRatedMovies() throws Exception {
        List<MovieCardDto> allMovies = new ArrayList<>();
        String TopRatedUrl = "top_rated?language=ko-KR&page=";

        for (int page = 1; page <= 5; page++) {
            Request request = new Request.Builder()
                    .url(TMDB_API_URL + TopRatedUrl + page + "&region=KR")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", AUTH_TOKEN )
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                log.info("MovieServiceImpl: "+jsonResponse);
                // JSON -> MovieCardDto 파싱
                MovieCardResponse movieList = gson.fromJson(jsonResponse, MovieCardResponse.class);

                allMovies.addAll(movieList.getResults());
            }
        }
        return allMovies;
    }

    @Override
    public List<MovieCardDto> getNowPlayingMovies() throws Exception {
        List<MovieCardDto> allMovies = new ArrayList<>();
        String NowPlayingUrl = "now_playing?language=ko-KR&page=";

        for (int page = 1; page <= 5; page++) {
            Request request = new Request.Builder()
                    .url(TMDB_API_URL + NowPlayingUrl + page + "&region=KR")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", AUTH_TOKEN )
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                log.info("MovieServiceImpl: "+jsonResponse);
                // JSON -> MovieCardDto 파싱
                MovieCardResponse movieList = gson.fromJson(jsonResponse, MovieCardResponse.class);

                allMovies.addAll(movieList.getResults());
            }
        }
        return allMovies;
    }

    @Override
    public List<MovieCardDto> getUpComingMovies() throws Exception {
        List<MovieCardDto> allMovies = new ArrayList<>();
        String NowPlayingUrl = "upcoming?language=ko-KR&page=";

        for (int page = 1; page <= 5; page++) {
            Request request = new Request.Builder()
                    .url(TMDB_API_URL + NowPlayingUrl + page + "&region=KR")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", AUTH_TOKEN )
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                log.info("MovieServiceImpl: "+jsonResponse);
                // JSON -> MovieCardDto 파싱
                MovieCardResponse movieList = gson.fromJson(jsonResponse, MovieCardResponse.class);

                allMovies.addAll(movieList.getResults());
            }
        }
        return allMovies;
    }

    @Override
    public MovieDetailsDto getMovieDetails(Long id) throws Exception {
        log.info("MovieServiceImpl: 지금 영화 데이터 TMDB에서 가져오는 중");
        String MovieDetailUrl = TMDB_API_URL + id + "?append_to_response=credits%2Cvideos%2Crecommendations&language=ko-KR";//detail & videos & recommendations

        Request request = new Request.Builder()
                .url(MovieDetailUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", AUTH_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                
                // 영화 상세정보 뽑아내기
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
                String title = jsonObject.get("title").getAsString();
                int runtime = jsonObject.get("runtime").getAsInt();
                String overview = jsonObject.get("overview").getAsString();
                String releaseDate = jsonObject.get("release_date").getAsString();
                String posterPath = jsonObject.get("poster_path").getAsString();
                String backdropPath = jsonObject.get("backdrop_path").getAsString();

                // 이미지 리스트 설정
                List<MovieDetailsDto.Images> imagesList = new ArrayList<>();
                MovieDetailsDto.Images images = new MovieDetailsDto.Images();
                images.setPoster_path(posterPath);
                images.setBackdrop_path(backdropPath);
                imagesList.add(images);

                // 장르 리스트 설정
                List<MovieDetailsDto.Genres> genres = new ArrayList<>();
                jsonObject.getAsJsonArray("genres").forEach(genreElement -> {
                    JsonObject genreObject = genreElement.getAsJsonObject();
                    MovieDetailsDto.Genres genre = new MovieDetailsDto.Genres();
                    genre.setId(genreObject.get("id").getAsInt());
                    genre.setName(genreObject.get("name").getAsString());
                    genres.add(genre);
                });

                // 비디오 리스트 설정
                List<MovieDetailsDto.Videos> videosList = new ArrayList<>();
                jsonObject.getAsJsonObject("videos").getAsJsonArray("results").forEach(videoElement -> {
                    JsonObject videoObject = videoElement.getAsJsonObject();
                    MovieDetailsDto.Videos video = new MovieDetailsDto.Videos();
                    video.setKey(videoObject.get("key").getAsString());
                    video.setType(videoObject.get("type").getAsString());
                    videosList.add(video);
                });

                // credit 리스트 설정 배우 상위 10명
                List<MovieDetailsDto.Credits> credits = new ArrayList<>();
                JsonArray castArray = jsonObject.getAsJsonObject("credits").getAsJsonArray("cast");
                JsonArray crewArray = jsonObject.getAsJsonObject("credits").getAsJsonArray("crew");

                // 배우 정보: 기본적으로 최대 10명, 10명 미만일 경우 전체 배우 추가
                int castCount = Math.min(10, castArray.size());
                for (int i = 0; i < castArray.size(); i++) {
                    if (i >= castCount) break;

                    JsonObject creditObject = castArray.get(i).getAsJsonObject();
                    MovieDetailsDto.Credits credit = new MovieDetailsDto.Credits();

                    credit.setType(creditObject.has("known_for_department") && !creditObject.get("known_for_department").isJsonNull()
                            ? creditObject.get("known_for_department").getAsString()
                            : "Unknown");

                    credit.setName(creditObject.has("name") && !creditObject.get("name").isJsonNull()
                            ? creditObject.get("name").getAsString()
                            : "Unknown");

                    credit.setProfile(creditObject.has("profile_path") && !creditObject.get("profile_path").isJsonNull()
                            ? creditObject.get("profile_path").getAsString()
                            : null);

                    credits.add(credit);
                }

                // 감독 정보: crewArray에서 job 필드가 "Director"인 인물 1명을 찾아서 리스트에 추가
                for (int i = 0; i < crewArray.size(); i++) {
                    JsonObject crewObject = crewArray.get(i).getAsJsonObject();

                    if (crewObject.has("job") && !crewObject.get("job").isJsonNull() && "Director".equals(crewObject.get("job").getAsString())) {
                        MovieDetailsDto.Credits director = new MovieDetailsDto.Credits();

                        director.setType("Director");
                        director.setName(crewObject.has("name") && !crewObject.get("name").isJsonNull()
                                ? crewObject.get("name").getAsString()
                                : "Unknown");

                        director.setProfile(crewObject.has("profile_path") && !crewObject.get("profile_path").isJsonNull()
                                ? crewObject.get("profile_path").getAsString()
                                : null);

                        credits.add(director); // 감독 정보를 배우 리스트에 추가
                        break; // 감독을 찾았으므로 루프 종료
                    }
                }



                // recommendations 리스트
                List<MovieDetailsDto.Recommends> recommends = new ArrayList<>();
                jsonObject.getAsJsonObject("recommendations").getAsJsonArray("results").forEach(recommendsElement -> {
                    JsonObject recommendsObject = recommendsElement.getAsJsonObject();
                    MovieDetailsDto.Recommends recommend = new MovieDetailsDto.Recommends();
                    recommend.setId(recommendsObject.get("id").getAsLong());
                    recommends.add(recommend);
                });


                // JSON 문자열로 변환
                String imagesJson = gson.toJson(imagesList);
                String videosJson = gson.toJson(videosList);
                String genresJson = gson.toJson(genres);


                MovieDetailsDto movieDetailsDto = new MovieDetailsDto(id, title, overview, releaseDate, runtime, imagesJson, videosJson, genresJson, credits, recommends);
                return movieDetailsDto; //화면에 보여주기
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }

    @Override
    public List<Long> SaveTopRatedId() throws Exception {
        List<Long> TopRatedMoviesId = new ArrayList<>();
        String TopRatedUrl = "top_rated?language=ko-KR&page=";

        for (int page = 1; page <= 500; page++) {
            Request request = new Request.Builder()
                    .url(TMDB_API_URL + TopRatedUrl + page + "&region=KR")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", AUTH_TOKEN)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

                // results 배열에서 영화 ID 뽑기
                jsonObject.getAsJsonArray("results").forEach(movieElement -> {
                    JsonObject movieObject = movieElement.getAsJsonObject();
                    Long movieId = movieObject.get("id").getAsLong();
                    TopRatedMoviesId.add(movieId);

                    // db에 저장
                    TopRatedMovieIdEntity topRatedMovieIdEntity = new TopRatedMovieIdEntity(movieId);
                    topRatedMovieIdRepository.save(topRatedMovieIdEntity);
                });
            }
        }
        return TopRatedMoviesId;
    }

    @Override
    public List<MovieDetailsDto> getTopRatedMovieDetails() throws Exception {
        List<MovieDetailsDto> movieDetailsList = new ArrayList<>();

        // DB에서 저장된 모든 영화 ID 가져오기
        List<Long> movieIds = topRatedMovieIdRepository.findAll()
                .stream()
                .map(TopRatedMovieIdEntity::getId)
                .collect(Collectors.toList());

        // 각 ID에 대해 영화 상세 정보 요청
        for (Long id : movieIds) {
            MovieDetailsDto movieDetails = getMovieDetails(id);
            movieRepository.save(toEntity(movieDetails)); //db에 저장

            // recommendations 저장
            for (MovieDetailsDto.Recommends recommId : movieDetails.getRecommendations()) {
                MovieRecommendDto recommendDto = MovieRecommendDto.builder()
                        .movieId(id)
                        .recommendationMovieId(recommId.getId())
                        .build();
                movieRecommendService.saveRecommendations(recommendDto);
            }

            // credits 저장
            for (MovieDetailsDto.Credits creditId : movieDetails.getCredits()) {
                MovieCreditsDto movieCreditsDto = MovieCreditsDto.builder()
                        .movieId(id)
                        .name(creditId.getName())
                        .type(creditId.getType())
                        .profile(creditId.getProfile())
                        .build();
                movieCreditService.saveMovieCredit(movieCreditsDto);
            }

            movieDetailsList.add(movieDetails);
        }


        return movieDetailsList;
    }

    @Override
    @Transactional
    public MovieDetailsDto getTopRatedMovieDetailsInDB(Long movieId) throws Exception{
        log.info("MovieServiceImpl: 지금 영화 데이터 DB에서 id로 검색");
        Optional<MovieDetailEntity> movieDetail = movieRepository.findById(movieId);
        MovieDetailEntity movieDetailEntity = movieDetail.orElseThrow();
        return toDto(movieDetailEntity);
    }

    @Override
    public MovieDetailsDto searchMovie(String title) throws Exception{
        log.info("MovieServiceImpl: 지금 영화 데이터 DB에서 이름으로 검색");
        Optional<MovieDetailEntity> movieDetail = movieRepository.findByTitle(title);
        MovieDetailEntity movieDetailEntity = movieDetail.orElseThrow();
        return toDto(movieDetailEntity);
    }

    @Override
    public List<MovieCardDto> searchByQuery(String query) {
        List<MovieDetailEntity> movieDetail = movieRepository.findByTitleContaining(query);
        return movieDetail.stream()
                .map(this::toMovieCardDto)
                .collect(Collectors.toList());
    }

    private MovieCardDto toMovieCardDto(MovieDetailEntity movieDetailEntity) {
        return MovieCardDto.builder()
                .id(movieDetailEntity.getId())
                .title(movieDetailEntity.getTitle())
                .overview(movieDetailEntity.getOverview())
                .poster_path(movieDetailEntity.getImages()) // or use another field for poster_path
                .release_date(movieDetailEntity.getRelease_date())
                .build();
    }

    private List<Integer> parseGenres(String genres) {
        // Assuming genres are stored as a comma-separated string of genre IDs, e.g., "1,2,3"
        return Arrays.stream(genres.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
