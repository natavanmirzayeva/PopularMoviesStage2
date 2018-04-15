package com.project.udacity.popularmoviesstage2.data;

import com.google.gson.JsonObject;
import com.project.udacity.popularmoviesstage2.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mehseti on 21.2.2018.
 */

public interface MoviesApi
{
    @GET("movie/{movieType}/?")
    public io.reactivex.Observable<MovieResponse> listMovies(@Path("movieType") String movieType, @Query("api_key") String keyApi);

    @GET("movie/{id}/videos?")
    public io.reactivex.Observable<JsonObject> getVideos(@Path("id") int id, @Query("api_key") String keyApi);

    @GET("movie/{id}/reviews?")
    public io.reactivex.Observable<JsonObject> getReviews(@Path("id") int id, @Query("api_key") String keyApi);
}
