package com.wevands.showhall.room.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wevands.showhall.model.Genres;
import com.wevands.showhall.model.ListMovies;
import com.wevands.showhall.room.mTypeConverters;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Movies {
    @NonNull
    @PrimaryKey
    private int movieId;
    private String movieName;
    private String originalTitle;
    private String tagline;
    private String overview;
    private boolean adult;
    private String posterPath;
    private String backdropPath;
    private String releaseDate;
    private boolean video;
    private String voteAverage;
    private int voteCount;
    @TypeConverters(mTypeConverters.class)
    private List<Genres> genres;

    public Movies() {
    }

    public Movies(int id, String title, String originalTitle, String tagline, String overview, boolean adult, String posterPath, String backdropPath, String releaseDate, boolean video, String voteAverage, int voteCount, List<Genres> genres) {
        this.movieId = id;
        this.movieName = title;
        this.originalTitle = originalTitle;
        this.tagline = tagline;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.genres = genres;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String  voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}