package com.example.tombolator;

import androidx.room.TypeConverter;
import com.example.tombolator.media.Media;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<Media> fromJsonString(String json) {

        Type listOfMyClassObject = new TypeToken<ArrayList<Media>>() {}.getType();

        List<Media> mediaList = new Gson().fromJson(json, listOfMyClassObject);
        return mediaList;
    }

    @TypeConverter
    public static String toJsonString(List<Media> mediaList) {
        return new Gson().toJson(mediaList);
    }
}