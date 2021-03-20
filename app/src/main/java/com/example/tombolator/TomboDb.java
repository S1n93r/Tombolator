package com.example.tombolator;

import androidx.room.RoomDatabase;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolaDao;

@androidx.room.Database(entities={
        Media.class,
        Tombola.class
}, version = 1)
public abstract class TomboDb extends RoomDatabase {

    public abstract MediaDao mediaDao();
    public abstract TombolaDao tombolaDao();

    public static final String NAME = "TomboDb";
}
