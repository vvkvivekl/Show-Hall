package com.wevands.showhall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wevands.showhall.DetailsActivity;
import com.wevands.showhall.R;
import com.wevands.showhall.model.Movie;
import com.wevands.showhall.model.MovieReviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    static List<MovieReviews> reviews;

    private int rowLayout;
    private Context context;


    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout moviesLayout;
        TextView movieTitle;
        TextView movieSub;
        ProgressBar progressBar;


        public ReviewViewHolder(View v) {
            super(v);
            moviesLayout = v.findViewById(R.id.reviews_layout);
            movieTitle = v.findViewById(R.id.reviews_title);
            movieSub = v.findViewById(R.id.reviews_subtitle);
            progressBar = v.findViewById(R.id.reviews_item_loading_pb);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "inside viewholder position = " + getAdapterPosition() + " " + credits.get(getAdapterPosition()).getId() , Toast.LENGTH_SHORT).show();
/*
                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    Movie dataToSend = new Movie();
                    intent.putExtra("id", reviews.get(getAdapterPosition()).getId());
                    intent.putExtra("movie", reviews.get(getAdapterPosition()));
                    intent.putExtra("position", getAdapterPosition());
                    v.getContext().startActivity(intent);
*/
                }
            });
        }



    }

    public ReviewsAdapter(List<MovieReviews> reviews, int rowLayout, Context context) {
        this.reviews = reviews;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ReviewViewHolder holder, final int position) {
        //String url = context.getResources().getString(R.string.url_mdb_img) + "w342/" + reviews.get(position).getPosterPath();

        holder.movieTitle.setText("@"+reviews.get(position).getAuthor());
        holder.movieSub.setText("\""+reviews.get(position).getContent()+"\"");
        holder.progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}