package com.wevands.showhall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.wevands.showhall.adapter.CreditsAdapter;
import com.wevands.showhall.adapter.ReviewsAdapter;
import com.wevands.showhall.model.MovieReviews;

import java.util.ArrayList;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity {
    private static final String TAG = ReviewsActivity.class.getSimpleName();
    List<MovieReviews> reviews = new ArrayList<MovieReviews>();
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        recyclerView = findViewById(R.id.reviewss_recycler_view);
        progressBar = findViewById(R.id.reviews_activity_loading_pb);
        Intent intent = getIntent();
        reviews = intent.getParcelableArrayListExtra("reviews");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ReviewsAdapter(reviews, R.layout.item_review, getApplicationContext()));
        Log.d(TAG, "\nParcel Received: \n" + "getAuthor() > " + reviews.get(0).getAuthor() + " \nsize() > " + reviews.size());
        progressBar.setVisibility(View.GONE);

    }
}
