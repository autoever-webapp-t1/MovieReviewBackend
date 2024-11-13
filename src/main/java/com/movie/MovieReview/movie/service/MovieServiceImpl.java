package com.movie.MovieReview.movie.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.dto.MovieCardResponse;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class MovieServiceImpl implements  MovieService{
    private final MovieRepository movieRepository;
    private final TopRatedMovieIdRepository topRatedMovieIdRepository;

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
//                for (MovieCardDto movie : movieList.getResults()) {
//                    SaveTopRated(movie);
//                }
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
        String MovieDetailUrl = TMDB_API_URL + id + "?append_to_response=videos%2Crecommendations&language=ko-KR";//detail & videos & recommendations

        Request request = new Request.Builder()
                .url(MovieDetailUrl)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", AUTH_TOKEN)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();

                log.info("MovieServiceImpl: 리스폰스바디.string() 값은? " +jsonResponse);
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

                // JSON 문자열로 변환
                String imagesJson = gson.toJson(imagesList);
                String videosJson = gson.toJson(videosList);


                MovieDetailsDto movieDetailsDto = new MovieDetailsDto(id, title, overview, releaseDate, runtime, imagesJson, videosJson);
                movieRepository.save(toEntity(movieDetailsDto)); //db에 저장
                return movieDetailsDto; //화면에 보여주기
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }

//    @Override
//    public void SaveMovieDetail(MovieDetailsDto movieDetailsDto) {
//        MovieDetailEntity movieDetailEntity = toEntity(movieDetailsDto);
//        movieRepository.save(movieDetailEntity);
//    }

    @Override
    public List<Long> SaveTopRatedId() throws Exception {
        List<Long> TopRatedMoviesId = new ArrayList<>();
        String TopRatedUrl = "top_rated?language=ko-KR&page=";

        for (int page = 1; page <= 5; page++) {
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
            movieDetailsList.add(movieDetails);
        }

        return movieDetailsList;
    }

}
