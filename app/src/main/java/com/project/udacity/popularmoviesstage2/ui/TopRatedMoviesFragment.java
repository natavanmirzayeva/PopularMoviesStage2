package com.project.udacity.popularmoviesstage2.ui;

import android.content.Context;
import android.content.Intent;
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
import com.project.udacity.popularmoviesstage2.data.MovieResponse;
import com.project.udacity.popularmoviesstage2.data.MoviesApi;
import com.project.udacity.popularmoviesstage2.utils.ApiVariables;
import com.project.udacity.popularmoviesstage2.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class TopRatedMoviesFragment extends Fragment
{
    @BindView(R.id.movies_recycler_view)
    RecyclerView recyclerView;
    static Context fContext;
    GridLayoutManager gridLayoutManager;
    private static final String RECYCLERVIEW_STATE = "recyclerview-state-1";
    private static final String RECYCLERVIEW_STATE_ADAPTER = "recyclerview-state-adapter";
    Parcelable savedRecyclerLayoutState;
    static OkHttpClient httpClient;
    static io.reactivex.Observable<MovieResponse> call;
    static MoviesAdapter moviesAdapter;
    private Disposable disposable;

    public static TopRatedMoviesFragment newInstance()
    {
        Bundle args = new Bundle();
        TopRatedMoviesFragment fragment = new TopRatedMoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_top_rated_movies, container, false);
        ButterKnife.bind(this,view);
        parseJson(getString(R.string.topRated));
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        return  view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLERVIEW_STATE, recyclerView.getLayoutManager().onSaveInstanceState());
        List<Movie> movie = moviesAdapter.getMovies();
        if (movie != null && !movie.isEmpty()) {
            outState.putParcelableArrayList(RECYCLERVIEW_STATE_ADAPTER, (ArrayList<? extends Parcelable>) movie);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLERVIEW_STATE);
            if (savedInstanceState.containsKey(RECYCLERVIEW_STATE_ADAPTER)) {
                List<Movie> movieResultList = savedInstanceState.getParcelableArrayList(RECYCLERVIEW_STATE_ADAPTER);
                moviesAdapter.setMovies(movieResultList);
                recyclerView.setAdapter(moviesAdapter);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (savedRecyclerLayoutState != null)
                    {
                        if (savedInstanceState.containsKey(RECYCLERVIEW_STATE_ADAPTER)) {
                            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
                            savedRecyclerLayoutState = null;
                        }
                    }
                }
            }, 200);
        }
    }
    public  void parseJson(String movieType)
    {
        httpClient = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiVariables.moviePopular)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =  builder.client(httpClient).build();
        MoviesApi moviesApi =  retrofit.create(MoviesApi.class);
        call = moviesApi.listMovies(movieType,ApiVariables.apiKey).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        call.map(new Function<MovieResponse, List<Movie>>() {
            @Override
            public List<Movie> apply(MovieResponse movieResponse) throws Exception {
                return movieResponse.getMovies();
            }
        })
                .subscribe
                        (
                                new Consumer<List<Movie>>() {
                                    @Override
                                    public void accept(List<Movie> movies) throws Exception
                                    {
                                        displayMovies(movies);
                                    }
                                },
                                new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.i(TAG, "RxJava2, HTTP Error: " + throwable.getMessage());
                                    }
                                }
                        );
    }

    void displayMovies(List<Movie> movieList)
    {
        moviesAdapter = new MoviesAdapter(movieList, R.layout.movie_list_item, getContext(), new MoviesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("movie", movie);
                getContext().startActivity(intent);
            }
        });
        recyclerView.setAdapter(moviesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        parseJson(getString(R.string.topRated));
    }
}
