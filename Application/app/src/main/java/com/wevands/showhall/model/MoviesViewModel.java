package com.wevands.showhall.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.wevands.showhall.room.DaoAccess;
import com.wevands.showhall.room.MovieDatabase;
import com.wevands.showhall.room.model.Movies;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MoviesViewModel extends AndroidViewModel {

    private DaoAccess daoAccess;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        daoAccess = MovieDatabase.getINSTANCE(application).daoAccess();
    }

    public LiveData<List<Movies>> getAllMovies() {
        return daoAccess.fetchAllMovies();
    }
    public void addMovie(Movies movie){
        daoAccess.insertOnlySingleMovie(movie);
    }
    public void deleteMovies(Movies movie){
        daoAccess.deleteMovie(movie);
    }
    public Movies getMovie(int movieId){
        return daoAccess.fetchOneMoviesbyMovieId(movieId);
    }
}
