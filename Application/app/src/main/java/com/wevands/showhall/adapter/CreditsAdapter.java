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
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wevands.showhall.DetailsActivity;
import com.wevands.showhall.R;
import com.wevands.showhall.model.MovieCredits;

import java.util.List;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.MovieViewHolder> {

    static List<MovieCredits> credits;

    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout creditsLayout;
        TextView creditTitle;
        ImageView imageView;
        ProgressBar progressBar;


        public MovieViewHolder(View v) {
            super(v);
            creditsLayout = v.findViewById(R.id.credits_layout);
            creditTitle = v.findViewById(R.id.list_credits_title);
            imageView = v.findViewById(R.id.list_credits_image_iv);
            progressBar = v.findViewById(R.id.list_credits_loading_pb);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), credits.get(getAdapterPosition()).getName() , Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    public CreditsAdapter(List<MovieCredits> credits, int rowLayout, Context context) {
        this.credits = credits;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public CreditsAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        String url = context.getResources().getString(R.string.url_mdb_img) + "w342/" + credits.get(position).getProfile_path();

        holder.creditTitle.setText(credits.get(position).getName());
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
        return credits.size();
    }
}