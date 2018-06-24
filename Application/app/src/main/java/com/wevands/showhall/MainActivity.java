package com.wevands.showhall;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.wevands.showhall.adapter.MoviesAdapter;
import com.wevands.showhall.heplers.Utility;
import com.wevands.showhall.model.ListMovies;
import com.wevands.showhall.model.Movie;
import com.wevands.showhall.utils.Api;
import com.wevands.showhall.utils.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static String API_KEY = "";
    RecyclerView recyclerView;

    ProgressBar progressBar;
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    TextView textViewMoviesType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        API_KEY = getResources().getString(R.string.mdb_api_key);
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_LONG).show();
            return;
        }
        recyclerView = findViewById(R.id.movies_recycler_view);
        progressBar = findViewById(R.id.main_activity_loading_pb);

        textViewMoviesType = findViewById(R.id.main_activity_movies_type_tv);

        /*
        Fab Source : https://www.viralandroid.com/2016/02/android-floating-action-menu-example.html
         */
        materialDesignFAM = findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                loadData(1);

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                loadData(2);
            }
        });


        loadData();
    }

    public void loadData() {
        loadData(1);
    }

    public void loadData(int requestType) {
        if (isOnline()) {
        /*
        Grid reference : https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
         */
            int numberOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

            ApiInterface apiService =
                    Api.getClient().create(ApiInterface.class);
            Call<ListMovies> call = apiService.getPopularMovies(API_KEY);
            switch (requestType) {
                case 1:
                    call = apiService.getPopularMovies(API_KEY);
                    textViewMoviesType.setText(this.getResources().getString(R.string.p_movies));
                    break;
                case 2:
                    call = apiService.getTopRatedMovies(API_KEY);
                    textViewMoviesType.setText(this.getResources().getString(R.string.tr_movies));
                    break;
            }
            call.enqueue(new Callback<ListMovies>() {
                @Override
                public void onResponse(Call<ListMovies> call, Response<ListMovies> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.item_movie, getApplicationContext()));
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
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
        }
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
