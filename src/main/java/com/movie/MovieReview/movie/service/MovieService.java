package com.movie.MovieReview.movie.service;

import com.google.gson.Gson;
import com.movie.MovieReview.movie.dto.MovieListResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public List<MovieListResponse> getPopularMovies() throws Exception {
        List<MovieListResponse> allMovies = new ArrayList<>();

        for (int page = 1; page <= 5; page++) {
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/top_rated?language=ko-KR&page=" + page)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMjUxYmI1M2Q5YTNkMTA0NGRiYTcwZDFiMmI2ZGEwNSIsInN1YiI6IjY2MmNmNDRlZjZmZDE4MDEyODIyNGI3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yGcscHFGjYQq6B7s_OqCif9IH5jw8vlFboOuJZNKnTk")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new Exception("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                // JSON 응답을 MovieListResponse 객체로 변환
                MovieListResponse movieList = gson.fromJson(jsonResponse, MovieListResponse.class);
                allMovies.add(movieList);
            }
        }
        return allMovies;
    }
}
