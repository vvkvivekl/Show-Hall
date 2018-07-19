package com.wevands.showhall.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieCredits implements Parcelable {

    @SerializedName("cast_id")
    private int id;
    @SerializedName("character")
    private String character;
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profile_path;

    public MovieCredits() {
    }

    public MovieCredits(int id, String character, String name, String profile_path) {
        this.id = id;
        this.character = character;
        this.name = name;
        this.profile_path = profile_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }


    protected MovieCredits(Parcel in) {
        id = in.readInt();
        character = in.readString();
        name = in.readString();
        profile_path = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(character);
        dest.writeString(name);
        dest.writeString(profile_path);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieCredits> CREATOR = new Parcelable.Creator<MovieCredits>() {
        @Override
        public MovieCredits createFromParcel(Parcel in) {
            return new MovieCredits(in);
        }

        @Override
        public MovieCredits[] newArray(int size) {
            return new MovieCredits[size];
        }
    };
}