package com.project.udacity.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mehseti on 7.4.2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String create_movie_table = "CREATE TABLE "+MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry.id + " INTEGER PRIMARY KEY," + MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " + MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL" +
                ");";
        final String create_trailer_table = "CREATE TABLE "+ MovieContract.MovieEntry.TRAILER_TABLE_NAME + " ("+
                MovieContract.MovieEntry.trailer_id+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_FOREIGN_MOVIE_ID+ " INTEGER," +
                MovieContract.MovieEntry.COLUMN_TRAILER_KEY + " TEXT NOT NULL," +
                " FOREIGN KEY("+MovieContract.MovieEntry.COLUMN_FOREIGN_MOVIE_ID+") REFERENCES movies(id)"+
                ");";


        final String create_review_table = "CREATE TABLE " + MovieContract.MovieEntry.REVIEW_TABLE_NAME + " (" +
                MovieContract.MovieEntry.review_id + " INTEGER PRIMARY KEY AUTOINCREMENT," + MovieContract.MovieEntry.COLUMN_FOREIGN_MOVIE_ID + " INTEGER,"+
                MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR + " TEXT," + MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT + " TEXT," +
                " FOREIGN KEY("+ MovieContract.MovieEntry.COLUMN_FOREIGN_MOVIE_ID+") REFERENCES movies(id) "+ ");";


        db.execSQL(create_movie_table);
        db.execSQL(create_trailer_table);
        db.execSQL(create_review_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TRAILER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.REVIEW_TABLE_NAME);
        onCreate(db);
    }
}
