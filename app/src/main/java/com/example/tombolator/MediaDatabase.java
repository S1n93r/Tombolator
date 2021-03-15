package com.example.tombolator;

import androidx.room.RoomDatabase;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;

@androidx.room.Database(entities={Media.class}, version = 1)
abstract class MediaDatabase extends RoomDatabase {

    public abstract MediaDao MediaDao();

    public static final String NAME = "MediaDatabase";
}
