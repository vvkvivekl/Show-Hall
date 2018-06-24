package com.wevands.showhall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wevands.showhall.model.Movie;
import com.wevands.showhall.utils.Api;
import com.wevands.showhall.utils.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private String API_KEY = "";
    private int id;
    private Movie movie;
    ImageView posterImageView, backdropImageView;
    TextView titleTextView, taglineTextView, overviewTextView, releaseDateTextView, voteAverageTextView, voteCountTextView;
    Context context = this;
    ProgressBar progressBar;
    private static final String TAG = DetailsActivity.class.getSimpleName();
    View someView;
    View root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //backdropImageView = findViewById(R.id.backdrop_iv);
        posterImageView = findViewById(R.id.poster_iv);
        // = findViewById(R.id.);
        titleTextView = findViewById(R.id.title_textView);
        taglineTextView = findViewById(R.id.tagline_textView);
        overviewTextView = findViewById(R.id.overview_textView);
        releaseDateTextView = findViewById(R.id.releaseDate_textView);
        voteAverageTextView = findViewById(R.id.voteAverage_textView);
        voteCountTextView = findViewById(R.id.voteCount_textView);
        progressBar = findViewById(R.id.details_activity_pb);
        someView = findViewById(R.id.randomViewInMainLayout);
        root = someView.getRootView();
        API_KEY = getResources().getString(R.string.mdb_api_key);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        ApiInterface apiService =
                Api.getClient().create(ApiInterface.class);
        Call<Movie> call = apiService.getMovieDetails(id, API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movie = response.body();
                PopulateUI(movie);
                Log.v(TAG, id + " " + movie.getTitle());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });


    }

    public void PopulateUI(Movie movie) {

        root.setBackgroundColor(getResources().getColor(R.color.theme4));
        titleTextView.setText(movie.getTitle());
        taglineTextView.setText(movie.getTagline());
        overviewTextView.setText(movie.getOverview());
        releaseDateTextView.setText(movie.getReleaseDate());
        voteAverageTextView.setText(movie.getVoteAverage() + "");
        voteCountTextView.setText(movie.getVoteCount() + "");

        /*
        Root view
        https://stackoverflow.com/questions/4761686/how-to-set-background-color-of-an-activity-to-white-programmatically
         */

        Picasso.with(context).load(getImageUrl(movie.getPosterPath(), null)).into(posterImageView);
        /*
        Set background in picasso
        https://stackoverflow.com/questions/29777354/how-do-i-set-background-image-with-picasso-in-code
         */
        Picasso.with(context).load(getImageUrl(movie.getPosterPath(), "92")).into(new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                /*
                Bitmap unblur = bitmap.copy(bitmap.getConfig(),true);
                posterImageView.setImageBitmap(unblur);
                 */
                // Get a handler that can be used to post to the main thread
                Handler mainHandler = new Handler(context.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        Bitmap blurredBitmap = blur(doColorFilter(bitmap, .5, .5, .5));
                        root.setBackground(new BitmapDrawable(context.getResources(), blurredBitmap));
                        progressBar.setVisibility(View.GONE);
                    }
                };
                mainHandler.post(myRunnable);


            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
            }
        });
    }

    public String getImageUrl(String imageName, @Nullable String width) {
        if (width == null) {
            width = "w342/";
        } else {
            width = "w" + width + "/";
        }
        return getResources().getString(R.string.url_mdb_img) + width + imageName;
    }

    public Bitmap blur(Bitmap image) {
        final float BLUR_RADIUS = 25f;

        if (null == image) return null;
        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);
        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }

    public static Bitmap doColorFilter(Bitmap src, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int) (Color.red(pixel) * red);
                G = (int) (Color.green(pixel) * green);
                B = (int) (Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
