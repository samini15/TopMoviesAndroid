package com.shayan.movies.api;

import com.shayan.movies.model.Movie;
import com.shayan.movies.model.MoviesResponse;
import com.shayan.movies.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String api_key);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String api_Key);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id") String movie_id, @Query("api_key") String api_Key);

    @GET("movie/{movie_id}/videos")
    Call<TrailersResponse> getTrailers(@Path("movie_id") String movie_id, @Query("api_key") String api_Key);
}
