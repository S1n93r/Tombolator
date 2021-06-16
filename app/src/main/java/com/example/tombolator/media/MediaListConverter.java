package com.example.tombolator.media;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MediaListConverter {

    @TypeConverter
    public static List<Media> fromJsonString(String json) {

        Type listOfMyClassObject = new TypeToken<ArrayList<Media>>() {}.getType();

        return new Gson().fromJson(json, listOfMyClassObject);
    }

    @TypeConverter
    public static String toJsonString(List<Media> mediaList) {
        return new Gson().toJson(mediaList);
    }
}