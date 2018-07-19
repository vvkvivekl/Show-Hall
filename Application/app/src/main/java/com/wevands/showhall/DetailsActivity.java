package com.wevands.showhall;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wevands.showhall.adapter.CreditsAdapter;
import com.wevands.showhall.adapter.GenresAdapter;
import com.wevands.showhall.adapter.VideosAdapter;
import com.wevands.showhall.model.Genres;
import com.wevands.showhall.model.ListCredits;
import com.wevands.showhall.model.ListReviews;
import com.wevands.showhall.model.ListVideos;
import com.wevands.showhall.model.Movie;
import com.wevands.showhall.model.MovieCredits;
import com.wevands.showhall.model.MovieReviews;
import com.wevands.showhall.model.MovieVideos;
import com.wevands.showhall.room.MovieDatabase;
import com.wevands.showhall.room.model.Movies;
import com.wevands.showhall.utils.Api;
import com.wevands.showhall.utils.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    Context context = this; // Context

    // Views
    ImageView posterImageView;
    TextView titleTextView, taglineTextView, overviewTextView, releaseDateTextView, reviewTextView, voteCountTextView;
    RatingBar ratingBar;
    ProgressBar progressBar;
    Button buttonFav;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView, recyclerViewGenres, recyclerViewVideos;
    View someView;
    View root;

    // Variables
    private String API_KEY = "";
    private static final String TAG = DetailsActivity.class.getSimpleName();
    Movie movie1 = new Movie(), movie3;
    MovieVideos movieVideos = new MovieVideos();
    List<MovieReviews> reviews = new ArrayList<MovieReviews>();
    // Actions
    int overviewType = 0;
    boolean isFavourite = false;

    // Rooms database
    private static final String DATABASE_NAME = "movies_db";
    private MovieDatabase movieDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        /*
         AppBar customization
         - Background transparent
         - Title Blank
         - Enable back button
          */
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // referencing views
        posterImageView = findViewById(R.id.poster_iv);
        titleTextView = findViewById(R.id.title_textView);
        taglineTextView = findViewById(R.id.tagline_textView);
        overviewTextView = findViewById(R.id.overview_textView);
        releaseDateTextView = findViewById(R.id.releaseDate_textView);
        reviewTextView = findViewById(R.id.voteAverage_textView);
        voteCountTextView = findViewById(R.id.voteCount_textView);
        progressBar = findViewById(R.id.details_activity_pb);
        recyclerView = findViewById(R.id.details_credits_recyclerView);
        recyclerViewGenres = findViewById(R.id.details_genres_recyclerView);
        recyclerViewVideos = findViewById(R.id.details_videos_recyclerView);
        someView = findViewById(R.id.randomViewInMainLayout);
        ratingBar = findViewById(R.id.details_activity_ratingBar);
        buttonFav = findViewById(R.id.buttonFav);
        floatingActionButton = findViewById(R.id.details_fab_fav);
        // Root view
        root = someView.getRootView();
        API_KEY = getResources().getString(R.string.mdb_api_key); // ------- API key from string.xml

        /*
        Get parcelable Intent
         */
        Intent intent = getIntent();
        movie1 = intent.getParcelableExtra("movie");
        Log.d(TAG, "Parcel Received: \n" + "getId() > " + movie1.getId() + " \ngetTitle() > " + movie1.getTitle());

        // initializing Room database
        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, DATABASE_NAME)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Check Movie added in favourite
                if (movieDatabase.daoAccess().fetchOneMoviesbyMovieId(movie1.getId()) != null) {
                    isFavourite = true; // set Favourite true
                    Log.d(TAG, "Database: " + movie1.getPosterPath() + " > "+ movieDatabase.daoAccess().fetchOneMoviesbyMovieId(movie1.getId()).getPosterPath() + " " + isFavourite);
                    favouriteBtn(); // refresh fav button
                    Movies movies = movieDatabase.daoAccess().fetchOneMoviesbyMovieId(movie1.getId());
                    Movie movie = new Movie(
                            movies.getMovieId(),
                            movies.getMovieName(),
                            movies.getOriginalTitle(),
                            movies.getTagline(),
                            movies.getOverview(),
                            movies.isAdult(),
                            movies.getPosterPath(),
                            movies.getBackdropPath(),
                            movies.getReleaseDate(),
                            movies.isVideo(),
                            Double.parseDouble(movies.getVoteAverage()),
                            movies.getVoteCount(),
                            movies.getGenres());
                }else {
                    favouriteBtn();

                }
            }
        }).start();

        PopulateUI(movie1);

        overviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (overviewType == 0) {
                    ViewGroup.LayoutParams params = overviewTextView.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    overviewTextView.setLayoutParams(params);
                    overviewTextView.setTextSize(14);
                    overviewTextView.setBackgroundColor(Color.parseColor("#000000"));
                    overviewType = 1;
                } else {
                    ViewGroup.LayoutParams params = overviewTextView.getLayoutParams();
                    params.height = 0;
                    overviewTextView.setLayoutParams(params);
                    overviewTextView.setTextSize(11);
                    overviewTextView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                    overviewType = 0;
                }
            }
        });

        reviewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviews.size()!=0) {
                    Intent intent = new Intent(v.getContext(), ReviewsActivity.class);
                    Movie dataToSend = new Movie();
                    intent.putParcelableArrayListExtra("reviews", (ArrayList<MovieReviews>) reviews);
                    startActivity(intent);
                }else {
                    Toast.makeText(DetailsActivity.this,"No Reviews",Toast.LENGTH_LONG).show();
                }

            }
        });

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favourite(isFavourite);
            }
        });
floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        favourite(isFavourite);
    }
});
        Log.e(TAG, "Favourite: " + isFavourite);
    }

    public void favourite(final boolean isFavouriteS) {
        if (isFavouriteS) {
            // Remove Movie from DB

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Movies movie = new Movies(movie1.getId(), movie1.getTitle(), movie1.getOriginalTitle(), movie1.getTagline(), movie1.getOverview(), movie1.isAdult(), movie1.getPosterPath(), movie1.getBackdropPath(), movie1.getReleaseDate(), movie1.isVideo(), String.valueOf(movie1.getVoteAverage()), movie1.getVoteCount(), movie3.getGenres());
                    movieDatabase.daoAccess().deleteMovie(movie);
                    isFavourite = false;
                    favouriteBtn();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailsActivity.this,"Removed Favourite",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        } else {
            // Add Movie to DB
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Movies movie = new Movies(movie1.getId(), movie1.getTitle(), movie1.getOriginalTitle(), movie1.getTagline(), movie1.getOverview(), movie1.isAdult(), movie1.getPosterPath(), movie1.getBackdropPath(), movie1.getReleaseDate(), movie1.isVideo(), String.valueOf(movie1.getVoteAverage()), movie1.getVoteCount(), movie3.getGenres());
                    movieDatabase.daoAccess().insertOnlySingleMovie(movie);
                    isFavourite = true;
                    favouriteBtn();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailsActivity.this,"Added to Favourite",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
        }
    }

    public void favouriteBtn(){
        if (isFavourite) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    floatingActionButton.setImageResource(R.drawable.if_favorite_delete_51907);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    floatingActionButton.setImageResource(R.drawable.if_favorite_add_51906);
                }
            });
        }
    }

    public void PopulateUI(Movie movie) {

        root.setBackgroundColor(getResources().getColor(R.color.theme4));
        titleTextView.setText(movie.getTitle());
        taglineTextView.setText(movie.getTagline());
        Log.d(TAG, "Tag: "+movie.getTagline());
        overviewTextView.setText(movie.getOverview());
        releaseDateTextView.setText(parseDateToddMMyyyy(movie.getReleaseDate()));
        reviewTextView.setText(movie.getVoteAverage() + "");

        float rating = (float) (movie.getVoteAverage() / 2);

        ratingBar.setRating(rating);

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ApiInterface apiService =
                Api.getClient().create(ApiInterface.class);
        Call<ListCredits> call = apiService.getMovieCredits(movie.getId(), API_KEY);
        call.enqueue(new Callback<ListCredits>() {
            @Override
            public void onResponse(Call<ListCredits> call, Response<ListCredits> response) {
                List<MovieCredits> credits = response.body().getCast();
                recyclerView.setAdapter(new CreditsAdapter(credits, R.layout.item_credits, getApplicationContext()));
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ListCredits> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        ApiInterface apiService2 =
                Api.getClient().create(ApiInterface.class);
        Call<ListReviews> call2 = apiService2.getMovieReviews(movie.getId(), API_KEY);
        call2.enqueue(new Callback<ListReviews>() {
            @Override
            public void onResponse(Call<ListReviews> call, Response<ListReviews> response) {
                reviews = response.body().getResults();

                final int min = 0;
                final int max = reviews.size();
                if (max > 0) {
                    if (max == 1) {
                        reviewTextView.setText("@"+reviews.get(0).getAuthor()+": \"" + reviews.get(0).getContent() + "\"");
                        voteCountTextView.setText("More 0");
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Random random = new Random();
                                final int r = random.nextInt((max - min) + 1) + min;
                                String rev = reviews.get(r).getContent();
                                Log.v(TAG, r + " >> " + rev);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        voteCountTextView.setText("More " + (max - 1));
                                        reviewTextView.setText("@"+reviews.get(r).getAuthor()+": \"" + reviews.get(r).getContent() + "\"");
                                    }
                                });

                            }
                        }).start();

                    }
                } else {
                    reviewTextView.setText("No Reviews");
                    voteCountTextView.setText("More 0");
                }
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ListReviews> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
        recyclerViewGenres.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ApiInterface apiService3 =
                Api.getClient().create(ApiInterface.class);
        Call<Movie> call3 = apiService3.getMovieDetails(movie.getId(), API_KEY);
        call3.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                movie3 = response.body();
                List<Genres> list = movie3.getGenres();
                Log.d(TAG, ">>>>>>>" + list.get(0).getName() + "<<<" + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                recyclerViewGenres.setAdapter(new GenresAdapter(list, R.layout.item_genres, getApplicationContext()));


            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ApiInterface apiService4 =
                Api.getClient().create(ApiInterface.class);
        Call<ListVideos> call4 = apiService4.getMovieVideos(movie.getId(), API_KEY);
        call4.enqueue(new Callback<ListVideos>() {
            @Override
            public void onResponse(Call<ListVideos> call, Response<ListVideos> response) {
                List<MovieVideos> list = response.body().getResults();
                Log.d(TAG, ">>>>>>>" + list.get(0).getName() + "<<<" + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                recyclerViewVideos.setAdapter(new VideosAdapter(list, R.layout.item_video, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<ListVideos> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
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
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
