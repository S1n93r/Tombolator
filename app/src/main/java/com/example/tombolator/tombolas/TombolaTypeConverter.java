package com.example.tombolator.tombolas;

import androidx.room.TypeConverter;

public class TombolaTypeConverter {

    @TypeConverter
    public static Tombola.Type fromJsonString(String type) {
        return Tombola.Type.valueOf(type);
    }

    @TypeConverter
    public static String toJsonString(Tombola.Type type) {
        return type.toString();
    }
}