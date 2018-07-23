package com.wevands.showhall;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.wevands.showhall.adapter.MoviesAdapter;
import com.wevands.showhall.heplers.Utility;
import com.wevands.showhall.model.ListMovies;
import com.wevands.showhall.model.Movie;
import com.wevands.showhall.model.MoviesViewModel;
import com.wevands.showhall.room.MovieDatabase;
import com.wevands.showhall.room.model.Movies;
import com.wevands.showhall.utils.Api;
import com.wevands.showhall.utils.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String API_KEY = "";
    RecyclerView recyclerView;
    GridLayoutManager manager;
    ProgressBar progressBar;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    Boolean isScrolling = false;
    TextView textViewMoviesType;
    int currentItems, totalItems, scrollOutItems, pageMovie = 1, totalPage = 1, movieRequestType = 1;
    List<Movie> movies = new ArrayList<Movie>();
    List<Movies> moviesList = new ArrayList<Movies>();
    MoviesAdapter moviesAdapter;
    // Rooms database
    private static final String DATABASE_NAME = "movies_db";
    //private MovieDatabase movieDatabase;
    boolean doubleBackToExitPressedOnce = false;
    private Parcelable recyclerViewState;
    MoviesViewModel moviesViewModel;
    private static final String LIFECYCLE_CALLBACK_MOVIELIST = "movie_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        API_KEY = getResources().getString(R.string.mdb_api_key);
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
            return;
        }
        // initializing Room database
        /*
        movieDatabase = Room.databaseBuilder(getApplicationContext(),
                MovieDatabase.class, DATABASE_NAME)
                .build();
*/
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        recyclerView = findViewById(R.id.movies_recycler_view);
        progressBar = findViewById(R.id.main_activity_loading_pb);
        int numberOfColumns = Utility.calculateNoOfColumns(getApplicationContext());

        textViewMoviesType = findViewById(R.id.main_activity_movies_type_tv);
        manager = new GridLayoutManager(this, numberOfColumns);

        recyclerView.setLayoutManager(manager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (movieRequestType != 3) {
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (movieRequestType != 3) {
                    currentItems = manager.getChildCount();

                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstVisibleItemPosition();
                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        isScrolling = false;
                        if (pageMovie == 1 || pageMovie < totalPage) {
                            pageMovie = pageMovie + 1;
                            loadData();
                        }
                    }
                }
            }
        });
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACK_MOVIELIST)) {
                movies = savedInstanceState.getParcelableArrayList(LIFECYCLE_CALLBACK_MOVIELIST);
                moviesAdapter = new MoviesAdapter(movies, R.layout.item_movie, getApplicationContext());
                recyclerView.setAdapter(moviesAdapter);
            }
        }
        /*
        Fab Source : https://www.viralandroid.com/2016/02/android-floating-action-menu-example.html
         */
        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                movies.clear();
                movieRequestType = 1;
                loadData();

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                movies.clear();
                movieRequestType = 2;
                loadData();
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Check Movie added in favourite
                        /*
                        if (movieDatabase.daoAccess().fetchAllMovies() != null && movieDatabase.daoAccess().fetchAllMovies().size() != 0) {
                            moviesList = movieDatabase.daoAccess().fetchAllMovies();
//                    Log.d(TAG, moviesList.get(0).getMovieName());
                        }
                        movieRequestType = 3;
                        loadData();
                        */

                        movieRequestType = 3;
                        moviesViewModel.getAllMovies().observe(MainActivity.this, new Observer<List<Movies>>() {
                            @Override
                            public void onChanged(@Nullable List<Movies> movies) {
                                moviesList = movies;
                                Log.d(TAG, ">>>>>>>>>>\n\n>>>"+moviesList.size());
                                loadData();

                            }
                        });


                    }

                }).start();

                textViewMoviesType.setText("Favourite Movies");
            }
        });


        loadData();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Movie> lifecycleListView = movies;
        outState.putParcelableArrayList(LIFECYCLE_CALLBACK_MOVIELIST, (ArrayList<? extends Parcelable>) lifecycleListView);
    }

    public void loadData() {
        final int requestType = movieRequestType;
        if (isOnline()) {
        /*
        Grid reference : https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
         */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressBar.setVisibility(View.VISIBLE);

                    if (requestType == 3) {

                        if (moviesList.size() > 0) {

                            movies.clear();
                            for (int i = 0; i < moviesList.size(); i++) {

                                Log.d(TAG, "For " + moviesList.get(i).getMovieId());
                                ApiInterface apiService =
                                        Api.getClient().create(ApiInterface.class);
                                Call<Movie> call = apiService.getMovieDetails(moviesList.get(i).getMovieId(), API_KEY);
                                call.enqueue(new Callback<Movie>() {
                                    @Override
                                    public void onResponse(Call<Movie> call, Response<Movie> response) {

                                        Movie a = response.body();

                                        Log.d(TAG, a.getTitle());
                                        movies.add(a);
                                        Log.d(TAG, movies.get(0).getTitle());
                                        if (moviesList.size() == moviesList.size()) {
                                            Log.e(TAG, movies.size() + " size");
                                            moviesAdapter = new MoviesAdapter(movies, R.layout.item_movie, getApplicationContext());
                                            recyclerView.setAdapter(moviesAdapter);
                                        }
                                        if (progressBar != null) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Movie> call, Throwable t) {
                                        // Log error here since request failed
                                        Log.e(TAG, t.toString());
                                    }
                                });

                            }

                        } else {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Toast.makeText(MainActivity.this, "No Favourite movies", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        ApiInterface apiService =
                                Api.getClient().create(ApiInterface.class);
                        Call<ListMovies> call = apiService.getPopularMovies(API_KEY, pageMovie);
                        switch (requestType) {
                            case 1:
                                call = apiService.getPopularMovies(API_KEY, pageMovie);
                                textViewMoviesType.setText(MainActivity.this.getResources().getString(R.string.p_movies));
                                break;
                            case 2:
                                call = apiService.getTopRatedMovies(API_KEY, pageMovie);
                                textViewMoviesType.setText(MainActivity.this.getResources().getString(R.string.tr_movies));
                                break;
                        }
                        call.enqueue(new Callback<ListMovies>() {
                            @Override
                            public void onResponse(Call<ListMovies> call, Response<ListMovies> response) {
                                pageMovie = response.body().getPage();
                                List<Movie> a = response.body().getResults();
                                movies.addAll(a);
                                if (pageMovie == 1) {
                                    totalPage = response.body().getTotalPages();
                                    moviesAdapter = new MoviesAdapter(movies, R.layout.item_movie, getApplicationContext());
                                    recyclerView.setAdapter(moviesAdapter);
                                } else {
                                    moviesAdapter.notifyDataSetChanged();
                                }

                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<ListMovies> call, Throwable t) {
                                // Log error here since request failed
                                Log.e(TAG, t.toString());
                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    /*
        Network changing:
            https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
