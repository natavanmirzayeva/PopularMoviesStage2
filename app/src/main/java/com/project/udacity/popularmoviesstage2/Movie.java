package com.project.udacity.popularmoviesstage2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mehseti on 21.2.2018.
 */

public class Movie implements Parcelable
{
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("overview")
    public String overview; // plot synopsis
    @SerializedName("vote_average")
    public double voteAverage; // userRating
    @SerializedName("release_date")
    public String releaseDate;
    public int id;

    public Movie(String originalTitle, String posterPath, String overview, double voteAverage, String releaseDate, int id)
    {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.id = id;
    }
    public Movie(Parcel in)
    {
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
        this.releaseDate = in.readString();
        this.id = in.readInt();
    }
    public String getOriginalTitle() {
        return originalTitle;
    }
    public String getPosterPath() { return posterPath; }
    public String getOverview() {
        return overview;
    }
    public double getVoteAverage() {
        return voteAverage;
    }
    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeInt(id);
    }
    public static final Creator CREATOR  = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
