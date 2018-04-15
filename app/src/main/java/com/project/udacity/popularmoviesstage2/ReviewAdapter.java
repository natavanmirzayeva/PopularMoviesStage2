package com.project.udacity.popularmoviesstage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by mehseti on 6.4.2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>
{
    List<Review> reviews;
    static TextView author,content;

    public ReviewAdapter(@NonNull List<Review> objects)
    {
        reviews = objects;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        public ReviewViewHolder(View v, final Context context)
        {
            super(v);
            author =  v.findViewById(R.id.author);
            content =  v.findViewById(R.id.comment);
        }
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_review_item, parent, false);
        return new ReviewViewHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position)
    {
      Review review = reviews.get(position);
      author.setText(review.getAuthor());
      content.setText(review.getComment());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

}
