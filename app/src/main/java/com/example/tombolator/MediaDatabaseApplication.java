package com.example.tombolator;

import android.app.Application;
import androidx.room.Room;

public class MediaDatabaseApplication extends Application {

    private MediaDatabase mediaDatabase;

    @Override
    public void onCreate() {

        super.onCreate();

        mediaDatabase = Room.inMemoryDatabaseBuilder(this, MediaDatabase.class)
                .fallbackToDestructiveMigration().build();
    }
}