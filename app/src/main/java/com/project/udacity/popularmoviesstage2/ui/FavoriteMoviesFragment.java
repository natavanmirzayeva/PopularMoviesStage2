package com.project.udacity.popularmoviesstage2.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.udacity.popularmoviesstage2.Movie;
import com.project.udacity.popularmoviesstage2.MoviesAdapter;
import com.project.udacity.popularmoviesstage2.R;
import com.project.udacity.popularmoviesstage2.data.MovieContract;
import com.project.udacity.popularmoviesstage2.data.MovieDbHelper;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteMoviesFragment extends Fragment
{

    private SQLiteDatabase sqLiteDatabase;
    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    private static final String RECYCLERVIEW_STATE = "recyclerview-state";
    Parcelable savedRecyclerLayoutState;
    ArrayList<Movie> movies;

    public FavoriteMoviesFragment() {}

    public static FavoriteMoviesFragment newInstance()
    {
        FavoriteMoviesFragment fragment = new FavoriteMoviesFragment();
        Bundle args = new Bundle();
        args.getInt("position");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        ButterKnife.bind(this, view);
        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        sqLiteDatabase = movieDbHelper.getWritableDatabase();
        getAllMovies();
        return view;
    }

   private  void getAllMovies()
   {
       movies = new ArrayList<>();
       Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
       if (cursor != null && cursor.moveToFirst())
       {
           do
           {
               int index;
               index = cursor.getColumnIndexOrThrow("id");
               int id = cursor.getInt(index);
               index = cursor.getColumnIndexOrThrow("original_title");
               String original_title = cursor.getString(index);
               index = cursor.getColumnIndexOrThrow("overview");
               String overview = cursor.getString(index);
               index = cursor.getColumnIndexOrThrow("poster_path");
               String poster_path = cursor.getString(index);
               index = cursor.getColumnIndexOrThrow("release_date");
               String release_date = cursor.getString(index);
               index = cursor.getColumnIndexOrThrow("vote_average");
               double vote_average = cursor.getDouble(index);
               movies.add(new Movie(original_title, poster_path, overview, vote_average, release_date, id));
           }
           while ((cursor.moveToNext()));
       }
       cursor.close();
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.movie_list_item, getContext() , new MoviesAdapter.OnItemClickListener()
               {
                   @Override
                   public void onItemClick(Movie movie)
                   {
                       Intent intent = new Intent(getContext(),DetailActivity.class);
                       intent.putExtra("movie",movie);
                       getContext().startActivity(intent);
                   }
               }));
               gridLayoutManager = new GridLayoutManager(getContext(),2);
               recyclerView.setLayoutManager(gridLayoutManager);
           }
       },200);
   }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLERVIEW_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (savedRecyclerLayoutState != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                        savedRecyclerLayoutState = null;
                    }
                }
            }, 400);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllMovies();
    }
}
