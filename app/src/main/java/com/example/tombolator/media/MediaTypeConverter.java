package com.example.tombolator.media;

import androidx.room.TypeConverter;

public class MediaTypeConverter {

    @TypeConverter
    public static MediaTypeEnum fromJsonString(String json) {
        return MediaTypeEnum.valueOf(json);
    }

    @TypeConverter
    public static String toJsonString(MediaTypeEnum mediaList) {
        return mediaList.name();
    }
}