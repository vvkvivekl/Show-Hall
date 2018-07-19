package com.wevands.showhall.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.wevands.showhall.model.Movie;
import com.wevands.showhall.model.MovieVideos;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    static List<MovieVideos> videos;

    private int rowLayout;
    private Context context;


    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout videosLayout;
        TextView videoTitle, videoSub;
        ImageView videosImageView;
        ProgressBar progressBar;


        public VideoViewHolder(View v) {
            super(v);
            videosLayout = v.findViewById(R.id.videos_layout);
            videoTitle = v.findViewById(R.id.videos_title);
            videoSub = v.findViewById(R.id.videos_subtitle);
            videosImageView = v.findViewById(R.id.videos_image_iv);
            progressBar = v.findViewById(R.id.videos_item_loading_pb);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Opening \""+ videos.get(getAdapterPosition()).getType() +"\" in YouTube App"  , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videos.get(getAdapterPosition()).getKey()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setPackage("com.google.android.youtube");
                    v.getContext().startActivity(intent);
                }
            });
        }



    }

    public VideosAdapter(List<MovieVideos> videos, int rowLayout, Context context) {
        this.videos = videos;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public VideosAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new VideoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final VideoViewHolder holder, final int position) {
        String url = context.getResources().getString(R.string.url_youtube_img) + videos.get(position).getKey() + "/hqdefault.jpg";

        holder.videoTitle.setText(videos.get(position).getName());
        holder.videoSub.setText(videos.get(position).getSite());
        Picasso.with(context).load(url).into(holder.videosImageView, new com.squareup.picasso.Callback() {
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
        return videos.size();
    }
}