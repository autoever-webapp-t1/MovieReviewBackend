//package com.movie.MovieReview.movie.service;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import com.movie.MovieReview.movie.dto.MovieDetailsDto;
//import com.movie.MovieReview.movie.dto.TopRatedResponse;
//import lombok.extern.log4j.Log4j2;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Log4j2
//@Service
//public class MovieServiceImpl implements  MovieService{
//
//    private final OkHttpClient client = new OkHttpClient();
//    private final Gson gson = new Gson();
//
//    private final String TMDB_API_URL = "https://api.themoviedb.org/3/movie/";
//    private final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwMjUxYmI1M2Q5YTNkMTA0NGRiYTcwZDFiMmI2ZGEwNSIsInN1YiI6IjY2MmNmNDRlZjZmZDE4MDEyODIyNGI3MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yGcscHFGjYQq6B7s_OqCif9IH5jw8vlFboOuJZNKnTk";
//
//    @Override
//    public List<TopRatedResponse> getTopRatedMovies() throws Exception {
//        List<TopRatedResponse> allMovies = new ArrayList<>();
//
//        String TopRatedUrl = "top_rated?language=ko-KR&page=";
//        String NowPlaying = "now_playing?language=ko-KR&page=";
//        String Popular = "popular?language=ko-KR&page=";
//        String Upcoming = "upcoming?language=ko-KR&page=";
//
//        for (int page = 1; page <= 5; page++) {
//            Request request = new Request.Builder()
//                    .url(TMDB_API_URL + TopRatedUrl + page)
//                    .get()
//                    .addHeader("accept", "application/json")
//                    .addHeader("Authorization", AUTH_TOKEN )
//                    .build();
//
//            try (Response response = client.newCall(request).execute()) {
//                if (!response.isSuccessful()) {
//                    throw new Exception("Unexpected code " + response);
//                }
//
//                String jsonResponse = response.body().string();
//                TopRatedResponse movieList = gson.fromJson(jsonResponse, TopRatedResponse.class);
//                allMovies.add(movieList);
//            }
//        }
//        return allMovies;
//    }
//
//    @Override
//    public MovieDetailsDto getMovieDetails(Long id) throws Exception {
//        String MovieDetailUrl = TMDB_API_URL + id + "?language=ko-KR";
//
//        Request request = new Request.Builder()
//                .url(MovieDetailUrl)
//                .get()
//                .addHeader("accept", "application/json")
//                .addHeader("Authorization", AUTH_TOKEN)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                String jsonResponse = response.body().string();
//
//                log.info("MovieServiceImpl: 리스폰스바디.string() 값은? " +jsonResponse);
//                // Parse JSON response
//                JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
//                String title = jsonObject.get("title").getAsString();
//                int runtime = jsonObject.get("runtime").getAsInt();
//                String overview = jsonObject.get("overview").getAsString();
//                String releaseDate = jsonObject.get("release_date").getAsString();
//
//                // Create and return MovieDetailsResponse
//                return new MovieDetailsDto(id,title,overview,releaseDate,runtime);
//            } else {
//                throw new IOException("Unexpected response code: " + response.code());
//            }
//        }
//    }
//
//
//}
