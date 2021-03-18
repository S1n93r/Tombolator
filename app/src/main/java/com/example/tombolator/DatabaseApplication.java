package com.example.tombolator;

import android.app.Application;
import androidx.room.Room;

public class DatabaseApplication extends Application {

    MediaDatabase mediaDatabase;

    @Override
    public void onCreate() {

        super.onCreate();

        mediaDatabase = Room.databaseBuilder(this, MediaDatabase.class, MediaDatabase.NAME)
                .fallbackToDestructiveMigration().build();
    }

    public MediaDatabase getMediaDatabase() {
        return mediaDatabase;
    }
}