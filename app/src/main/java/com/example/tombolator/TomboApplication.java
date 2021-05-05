package com.example.tombolator;

import android.app.Application;
import androidx.room.Room;

public class TomboApplication extends Application {

    TomboDatabase tomboDatabase;

    @Override
    public void onCreate() {

        super.onCreate();

        tomboDatabase = Room.databaseBuilder(this, TomboDatabase.class, TomboDatabase.NAME)
                .fallbackToDestructiveMigration().build();
    }

    public TomboDatabase getTomboDb() {
        return tomboDatabase;
    }
}