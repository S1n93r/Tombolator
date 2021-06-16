package com.example.tombolator.tombolas;

import androidx.room.TypeConverter;

public class TombolaTypeConverter {

    @TypeConverter
    public static Tombola.TombolaTypeConverter fromJsonString(String type) {
        return Tombola.TombolaTypeConverter.valueOf(type);
    }

    @TypeConverter
    public static String toJsonString(Tombola.TombolaTypeConverter type) {
        return type.toString();
    }
}