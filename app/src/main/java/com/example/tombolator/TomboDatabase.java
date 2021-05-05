package com.example.tombolator;

import android.content.Context;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolaDao;

@androidx.room.Database(entities={
        Media.class,
        Tombola.class
}, version = 1)
@TypeConverters({Converters.class})
public abstract class TomboDatabase extends RoomDatabase {

    private static TomboDatabase instance;

    public abstract MediaDao mediaDao();
    public abstract TombolaDao tombolaDao();

    public static final String NAME = "TomboDb";

    public static synchronized TomboDatabase getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TomboDatabase.class, TomboDatabase.NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}