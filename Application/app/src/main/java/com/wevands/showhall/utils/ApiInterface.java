package com.wevands.showhall.utils;

import com.wevands.showhall.model.ListCredits;
import com.wevands.showhall.model.ListReviews;
import com.wevands.showhall.model.ListVideos;
import com.wevands.showhall.model.MovieCredits;
import com.wevands.showhall.model.ListMovies;
import com.wevands.showhall.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/popular")
    Call<ListMovies> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<ListMovies> getTopRatedMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<ListCredits> getMovieCredits(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ListReviews> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ListVideos> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);
}