package com.project.udacity.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mehseti on 7.4.2018.
 */

public class MovieContract
{
    MovieContract(){};
    public static final String AUTHORITY = "com.project.udacity.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_TRAILER = "trailers";
    public static final String PATH_REVIEWS = "reviews";

    public static class MovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final Uri CONTENT_URI_TRAILERS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();
        public static final Uri CONTENT_URI_REVIEWS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();
        public static final String TABLE_NAME = "movies";
        public static final String TRAILER_TABLE_NAME = "trailers";
        public static final String REVIEW_TABLE_NAME = "reviews";
        public static final String  COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String  COLUMN_POSTER_PATH= "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_RELEASE_DATE= "release_date";
        public static final String id = "id";
        public static final String trailer_id = "trailer_id";
        public static final String review_id = "review_id";
        public static final String COLUMN_TRAILER_KEY = "key";
        public static final String COLUMN_FOREIGN_MOVIE_ID = "id";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";
    }
}
