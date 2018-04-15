package com.project.udacity.popularmoviesstage2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.project.udacity.popularmoviesstage2.utils.ApiVariables;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mehseti on 2.4.2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder>
{
    public interface OnItemClickListener {
        void onItemClick(String key);
    }

    private static List<String> trailers;
    private int rowLayout;
    private Context context;
    private final OnItemClickListener listener;

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moviesLayout;
        ImageView movieImage;
        View movieItemView;
        ProgressBar progressBar;
        ImageButton imageButton;

        public TrailerViewHolder(View v, final Context context) {
            super(v);
            moviesLayout = v.findViewById(R.id.movie_list);
            movieImage = v.findViewById(R.id.poster_path);
            progressBar = v.findViewById(R.id.progressBar);
            imageButton = v.findViewById(R.id.play);
            movieItemView = v;
        }

        public void bind(final String key, final TrailersAdapter.OnItemClickListener listener)
        {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(key);
                }
            });
        }
    }

    public TrailersAdapter(ArrayList<String> keys, int rowLayout, Context context, OnItemClickListener listener)
    {
        this.trailers = keys;
        this.rowLayout = rowLayout;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public TrailersAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TrailerViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(final TrailersAdapter.TrailerViewHolder holder, int position) {
        holder.bind(trailers.get(position), listener);
        String key = trailers.get(position).replaceAll("^\"|\"$" ,"");

        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.with(context).load(ApiVariables.youtubeImageThumbnail+key+ApiVariables.ThumbnailEnd).into(holder.movieImage, new Callback() {
            @Override
            public void onSuccess()
            {
                if(holder.progressBar != null) holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }


}
