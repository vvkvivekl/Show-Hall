package com.wevands.showhall.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListVideos {

    @SerializedName("id")
    private int page;
    @SerializedName("results")
    private List<MovieVideos> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieVideos> getResults() {
        return results;
    }

    public void setResults(List<MovieVideos> results) {
        this.results = results;
    }

}
