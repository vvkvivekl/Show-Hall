package com.wevands.showhall.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.wevands.showhall.room.model.Movies;

@Database(entities = {Movies.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}
