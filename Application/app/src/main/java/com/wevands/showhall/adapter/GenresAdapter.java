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
import com.wevands.showhall.model.Genres;
import com.wevands.showhall.model.MovieCredits;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MovieViewHolder> {

    static List<Genres> genres;

    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout creditsLayout;
        TextView genresName;


        public MovieViewHolder(View v) {
            super(v);
            creditsLayout = v.findViewById(R.id.genres_layout);
            genresName = v.findViewById(R.id.list_genres_title);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "inside viewholder position = " + getAdapterPosition() + " " + credits.get(getAdapterPosition()).getId() , Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    public GenresAdapter(List<Genres> genres, int rowLayout, Context context) {
        this.genres = genres;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public GenresAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        String url = context.getResources().getString(R.string.url_mdb_img) + "w342/" + genres.get(position).getName();

        holder.genresName.setText(genres.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return genres.size();
    }
}