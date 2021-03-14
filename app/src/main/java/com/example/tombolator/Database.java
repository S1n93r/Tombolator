package com.example.tombolator;

import androidx.room.RoomDatabase;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;

@androidx.room.Database(entities={Media.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract MediaDao MediaDao();

    public static final String NAME = "Database";
}
