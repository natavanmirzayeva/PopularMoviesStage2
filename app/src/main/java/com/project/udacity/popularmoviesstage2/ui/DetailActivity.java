package com.project.udacity.popularmoviesstage2.ui;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.udacity.popularmoviesstage2.Movie;
import com.project.udacity.popularmoviesstage2.R;
import com.project.udacity.popularmoviesstage2.Review;
import com.project.udacity.popularmoviesstage2.ReviewAdapter;
import com.project.udacity.popularmoviesstage2.TrailersAdapter;
import com.project.udacity.popularmoviesstage2.data.MovieContract;
import com.project.udacity.popularmoviesstage2.data.MovieDbHelper;
import com.project.udacity.popularmoviesstage2.data.MoviesApi;
import com.project.udacity.popularmoviesstage2.utils.ApiVariables;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.poster_path)ImageView posterPath;
    @BindView(R.id.overview) TextView overView;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.user_rating_value) TextView userRating;
    @BindView(R.id.release_date_value) TextView releaseDate;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.movies_recycler_view)RecyclerView recyclerView;
    @BindView(R.id.review_list)RecyclerView review_recycler;
    @BindView(R.id.fab)FloatingActionButton floatingActionButton;
    @BindView(R.id.NestedScrollView) NestedScrollView nestedScrollView;
    private SQLiteDatabase sqLiteDatabase;
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<Review> reviews = new ArrayList<>();
    OkHttpClient httpClient;
    io.reactivex.Observable<JsonObject> videsResponseCall;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Movie movieInfo = intent.getParcelableExtra("movie");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        sqLiteDatabase = movieDbHelper.getWritableDatabase();
        final Movie movie = intent.getParcelableExtra("movie");
        boolean control =  fillDetailScreen(movie);
        getMovieVideos(movie,control);
        getReviews(movie,control);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean response = getSingleItem(movie);
                if(response == true)
                {
                    floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    deleteMovie(movie);
                    deleteReviews(movie);
                }
                else
                {
                    floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                    addNewMovies(movie);
                }
            }
        });
    }

    private void addNewMovies(Movie movie)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.id,movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH,movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,movie.getReleaseDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,movie.getVoteAverage());
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,contentValues);
        addTrailersForMovies(movie);
        addReviewsForMovies(movie);
    }

    private void addTrailersForMovies(Movie movie)
    {
        for (int j = 0; j < keys.size(); j++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_FOREIGN_MOVIE_ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TRAILER_KEY, keys.get(j));
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI_TRAILERS, contentValues);
        }
    }

    private void addReviewsForMovies(Movie movie)
    {
        for (int j = 0; j < reviews.size(); j++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_FOREIGN_MOVIE_ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_AUTHOR, reviews.get(j).getAuthor());
            contentValues.put(MovieContract.MovieEntry.COLUMN_REVIEW_CONTENT, reviews.get(j).getComment());
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI_REVIEWS, contentValues);
        }
    }

    private void deleteMovie(Movie movie)
    {
        int id = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,"id = " + movie.getId(),null);
    }

    private void deleteReviews(Movie movie)
    {
        int id = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI_REVIEWS,"id = " + movie.getId(),null);
    }

    private boolean  getSingleItem(Movie movie)
    {

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,"id = " + movie.getId(),null,null);
        if(cursor.moveToFirst()) return true;
        else return false;
    }

    boolean fillDetailScreen(Movie movie)
    {
        overView.setText(movie.getOverview());
        title.setText(movie.getOriginalTitle());
        userRating.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());
        Picasso.with(this).load(ApiVariables.imagePath+movie.getPosterPath()).into(posterPath);
        boolean response = getSingleItem(movie);
        if(response == true) {
            floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
            return true;
        }
        else
        {
            floatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
            return false;
        }
    }

    void getMovieVideos(Movie movie,boolean control )
    {
        getVideos(movie.getId(),control);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    void  getReviews(Movie movie,boolean control)
    {
        getReviews(movie.getId(),control);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putIntArray("ARTICLE_SCROLL_POSITION",
                new int[]{ nestedScrollView.getScrollX(), nestedScrollView.getScrollY()});
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("ARTICLE_SCROLL_POSITION");
        if(position != null)
        {
            nestedScrollView.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    nestedScrollView.scrollTo(position[0], position[1]);
                }
            }, 600);
        }
    }

    public void getVideos(final int id, final boolean control)
    {
        if(control == true)
        {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI_TRAILERS,null,"id = " + id,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                keys.add(cursor.getString(cursor.getColumnIndex("key")));
            }
            displayTrailers(keys);
            return;
        }
        httpClient = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiVariables.moviePopular)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =  builder.client(httpClient).build();
        MoviesApi moviesApi =  retrofit.create(MoviesApi.class);
        videsResponseCall = moviesApi.getVideos(id,ApiVariables.apiKey).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        videsResponseCall.map(new Function<JsonObject, ArrayList<String>>() {

            @Override
            public ArrayList<String> apply(JsonObject jsonObjectResponse) throws Exception
            {
                keys.clear();
                JsonArray jsonArray = jsonObjectResponse.get("results").getAsJsonArray();
                for (int i=0;i<jsonArray.size();i++)
                {
                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                    String key = object.get("key").toString();
                    keys.add(key);
                }
                return keys;
            }
        })
        .subscribe(
                    new Consumer<ArrayList<String>>() {
                        @Override
                        public void accept(ArrayList<String> keys) throws Exception
                        {
                            displayTrailers(keys);
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

    void displayTrailers(ArrayList<String> keys)
    {
        this.keys = keys;
        recyclerView.setAdapter(new TrailersAdapter(keys, R.layout.trailers_list_item, getApplicationContext(), new TrailersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String key)
            {
                String keyNew = delete_quotes(key);
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiVariables.youtubeLink + keyNew));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(ApiVariables.youtubeLink + keyNew));
                try {
                    getApplicationContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    getApplicationContext().startActivity(webIntent);
                }
            }
        }));
    }

    public void getReviews(final int id, boolean control)
    {
        if(control == true)
        {
            Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI_REVIEWS,null,"id = " + id,null,null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                Review review = new Review();
                review.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                review.setComment(cursor.getString(cursor.getColumnIndex("content")));
                reviews.add(review);
            }
            displayReviews(reviews);
            return;
        }
        httpClient = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiVariables.moviePopular)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =  builder.client(httpClient).build();
        MoviesApi moviesApi =  retrofit.create(MoviesApi.class);
        videsResponseCall = moviesApi.getReviews(id,ApiVariables.apiKey).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        videsResponseCall.map(new Function<JsonObject, ArrayList<Review>>() {

            @Override
            public ArrayList<Review> apply(JsonObject jsonObjectResponse) throws Exception
            {
                reviews.clear();
                JsonArray jsonArray = jsonObjectResponse.get("results").getAsJsonArray();
                for (int i=0;i<jsonArray.size();i++)
                {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String author = jsonObject.get("author").toString();
                    String content = jsonObject.get("content").toString();
                    Review review = new Review();
                    review.setAuthor(delete_quotes(author));
                    review.setComment(delete_quotes(content));
                    reviews.add(review);
                }
                if(reviews.size() == 0)
                {
                    Review review = new Review();
                    review.setAuthor(delete_quotes(getString(R.string.noreviews)));
                    reviews.add(review);
                }
                return reviews;
            }
        })
                .subscribe(
                        new Consumer<ArrayList<Review>>() {
                            @Override
                            public void accept(ArrayList<Review> keys) throws Exception
                            {
                                displayReviews(reviews);
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

    void displayReviews(ArrayList<Review> reviews)
    {
        this.reviews = reviews;
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        review_recycler.setAdapter(reviewAdapter);
        review_recycler.setLayoutManager(linearLayoutManager);
        review_recycler.setNestedScrollingEnabled(false);
        review_recycler.setHasFixedSize(false);
    }

    static String delete_quotes(String word)
    {
        return word.replaceAll("^\"|\"$" ,"");
    }
}
