package com.project.udacity.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by mehseti on 7.4.2018.
 */

public class MovieContentProvider extends ContentProvider
{

    private  MovieDbHelper movieDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    public static final int TRAILERS = 102;
    public static final int TRAILERS_WITH_ID = 103;
    public static final int REVIEWS = 104;
    public static final int REVIEWS_WITH_ID = 105;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES ,MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES + "/#",MOVIES_WITH_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TRAILER ,TRAILERS);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TRAILER + "/#",TRAILERS_WITH_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_REVIEWS ,REVIEWS);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_REVIEWS + "/#",REVIEWS_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final  SQLiteDatabase sqLiteDatabase = movieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match)
        {
            case MOVIES:
                returnCursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MOVIES_WITH_ID:
                returnCursor =  sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case REVIEWS:
                returnCursor = sqLiteDatabase.query(MovieContract.MovieEntry.REVIEW_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case TRAILERS:
                returnCursor = sqLiteDatabase.query(MovieContract.MovieEntry.TRAILER_TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match)
        {
            case MOVIES:
                long id = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if(id > 0)
                {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into "+id);
                }
                break;
            case TRAILERS:
                long id_trailer = sqLiteDatabase.insert(MovieContract.MovieEntry.TRAILER_TABLE_NAME,null,values);
                if(id_trailer > 0)
                {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_TRAILERS,id_trailer);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into "+id_trailer);
                }
                break;
            case REVIEWS:
                long id_review = sqLiteDatabase.insert(MovieContract.MovieEntry.REVIEW_TABLE_NAME,null,values);
                if(id_review > 0)
                {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI_REVIEWS,id_review);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into "+id_review);
                }
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");

        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final  SQLiteDatabase sqLiteDatabase = movieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        int  returnId;
        switch (match)
        {
            case MOVIES:
                returnId = sqLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case MOVIES_WITH_ID:

                // Construct a query as you would normally, passing in the selection/args
                returnId =  sqLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case REVIEWS:
                returnId = sqLiteDatabase.delete(MovieContract.MovieEntry.REVIEW_TABLE_NAME,selection,selectionArgs);
                break;
            case REVIEWS_WITH_ID:

                // Construct a query as you would normally, passing in the selection/args
                returnId =  sqLiteDatabase.delete(MovieContract.MovieEntry.REVIEW_TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        //returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
