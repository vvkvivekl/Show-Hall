package com.wevands.showhall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wevands.showhall.DetailsActivity;
import com.wevands.showhall.R;
import com.wevands.showhall.model.ListMovies;
import com.wevands.showhall.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    static List<Movie> movies;

    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout moviesLayout;
        TextView movieTitle;
        //TextView data;
        TextView rating;
        ImageView imageView;
        ProgressBar progressBar;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = v.findViewById(R.id.movies_layout);
            movieTitle = v.findViewById(R.id.title);
            //data = v.findViewById(R.id.subtitle);
            rating = v.findViewById(R.id.rating);
            imageView = v.findViewById(R.id.image_iv);
            progressBar = v.findViewById(R.id.list_item_loading_pb);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "inside viewholder position = " + getAdapterPosition() + " " + credits.get(getAdapterPosition()).getId() , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    Movie dataToSend = new Movie();
                    intent.putExtra("id", movies.get(getAdapterPosition()).getId());
                    intent.putExtra("movie", movies.get(getAdapterPosition()));
                    intent.putExtra("position", getAdapterPosition());
                    v.getContext().startActivity(intent);

                }
            });
        }



    }

    public MoviesAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        String url = context.getResources().getString(R.string.url_mdb_img) + "w342/" + movies.get(position).getPosterPath();

        holder.movieTitle.setText(movies.get(position).getTitle());
        //holder.data.setText(reviews.get(position).getReleaseDate());
        holder.rating.setText(movies.get(position).getVoteAverage() + "");
        Picasso.with(context).load(url).into(holder.imageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (holder.progressBar != null) {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}