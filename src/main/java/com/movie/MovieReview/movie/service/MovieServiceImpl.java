package com.movie.MovieReview.movie.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.movie.MovieReview.movie.dto.MovieCardDto;
import com.movie.MovieReview.movie.dto.MovieDetailsDto;
import com.movie.MovieReview.movie.dto.TopRatedResponse;
import com.movie.MovieReview.movie.entity.MovieEntity;
import com.movie.MovieReview.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class MovieServiceImpl implements  MovieService{
    private final MovieRepository movieRepository;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    private final String TMDB_API_URL = "https://api.themoviedb.org/3/movie/";
    private final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMjUxYmI1M2Q5YTNkMTA0NGRiYTcwZDFiMmI2ZGEwNSIsInN1YiI6IjY2MmNmNDRlZjZmZDE4MDEyODIyNGI3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yGcscHFGjYQq6B7s_OqCif9IH5jw8vlFboOuJZNKnTk";

    @Override
    public List<TopRatedResponse> getTopRatedMovies() throws Exception {
        List<TopRatedResponse> allMovies = new ArrayList<>();

        String TopRatedUrl = "top_rated?language=ko-KR&page=";
        String NowPlaying = "now_playing?language=ko-KR&page=";
        String Popular = "popular?language=ko-KR&page=";
        String Upcoming = "upcoming?language=ko-KR&page=";

        for (int page = 1; page <= 500; page++) {
            Request request = new Request.Builder()
                    .url(TMDB_API_URL + TopRatedUrl + page)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", AUTH_TOKEN )
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                TopRatedResponse movieList = gson.fromJson(jsonResponse, TopRatedResponse.class);
                allMovies.add(movieList);

//                for (MovieCardDto movie : movieList.getResults()) {
//                    SaveTopRated(movie);
//                }
            }
        }
        return allMovies;
    }

    @Override
    public MovieDetailsDto getMovieDetails(Long id) throws Exception {
        String MovieDetailUrl = TMDB_API_URL + id + "?language=ko-KR";

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
                imagesList.add(images);  // Add the image paths to the list

                // 장르 리스트 설정
                List<MovieDetailsDto.Genre> genres = new ArrayList<>();
                jsonObject.getAsJsonArray("genres").forEach(genreElement -> {
                    JsonObject genreObject = genreElement.getAsJsonObject();
                    MovieDetailsDto.Genre genre = new MovieDetailsDto.Genre();
                    genre.setId(genreObject.get("id").getAsInt());
                    genre.setName(genreObject.get("name").getAsString());
                    genres.add(genre);
                });

                // Create and return MovieDetailsResponse
                return new MovieDetailsDto(id,title,overview,imagesList,releaseDate,runtime,genres);
            } else {
                throw new IOException("Unexpected response code: " + response.code());
            }
        }
    }

    @Override
    public void SaveTopRated(MovieCardDto movieCardDto) {
        MovieEntity movieEntity = toEntity(movieCardDto);
        movieRepository.save(movieEntity);
    }


}
