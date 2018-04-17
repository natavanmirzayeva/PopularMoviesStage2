package com.project.udacity.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.project.udacity.popularmoviesstage2.utils.ApiVariables;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mehseti on 24.2.2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>
{

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private  List<Movie> movies;
    private int rowLayout;
    private Context context;
    private final OnItemClickListener listener;

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout moviesLayout;
        ImageView movieImage;
        View movieItemView;

        public MovieViewHolder(View v, final Context context)
        {
            super(v);
            moviesLayout =  v.findViewById(R.id.movie_list);
            movieImage =  v.findViewById(R.id.poster_path);
            movieItemView = v;
        }

        public void bind(final Movie movie, final OnItemClickListener listener)
        {
            movieItemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }
    }

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context, OnItemClickListener listener)
    {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position)
    {
        holder.bind(movies.get(position), listener);
        Picasso.with(context).load(ApiVariables.imagePath+movies.get(position).getPosterPath()).into(holder.movieImage);
    }
    @Override
    public int getItemCount()
    {
        return movies.size();
    }

    public  List<Movie> getMovies()
    {
        return movies;
    }


    public void setMovies(List<Movie> movies)
    {
        this.movies = movies;
    }
}
