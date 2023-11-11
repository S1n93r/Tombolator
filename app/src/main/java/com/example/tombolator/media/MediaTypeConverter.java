package com.example.tombolator.media;

import androidx.room.TypeConverter;

public class MediaTypeConverter {

    @TypeConverter
    public static MediaType fromJsonString(String json) {
        return MediaType.valueOf(json);
    }

    @TypeConverter
    public static String toJsonString(MediaType mediaList) {
        return mediaList.name();
    }
}