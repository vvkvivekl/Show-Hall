package com.wevands.showhall.room;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wevands.showhall.model.Genres;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class mTypeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Genres> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Genres>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Genres> someObjects) {
        return gson.toJson(someObjects);
    }
}