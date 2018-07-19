package com.wevands.showhall.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCredits {

    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private List<MovieCredits> cast;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MovieCredits> getCast() {
        return cast;
    }

    public void setCast(List<MovieCredits> cast) {
        this.cast = cast;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
