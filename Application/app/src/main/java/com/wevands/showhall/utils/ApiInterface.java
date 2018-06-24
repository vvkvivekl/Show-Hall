package com.wevands.showhall.utils;

import com.wevands.showhall.model.ListMovies;
import com.wevands.showhall.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/popular")
    Call<ListMovies> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<ListMovies> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}