package com.project.udacity.popularmoviesstage2.data;

import com.google.gson.annotations.SerializedName;
import com.project.udacity.popularmoviesstage2.Movie;

import java.util.List;

/**
 * Created by mehseti on 21.2.2018.
 */

public class MovieResponse
{
    @SerializedName("results")
    List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
