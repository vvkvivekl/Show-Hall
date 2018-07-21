package com.wevands.showhall.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.wevands.showhall.room.model.Movies;
// Live data : https://medium.com/@guendouz/room-livedata-and-recyclerview-d8e96fb31dfe
@Database(entities = {Movies.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static MovieDatabase INSTANCE;
    public abstract DaoAccess daoAccess();
    private static final Object sLock = new Object();

    public static MovieDatabase getINSTANCE(Context context){
        synchronized (sLock){
            if (INSTANCE==null){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class,"Movies.db")
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

}
