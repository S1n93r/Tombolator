package com.example.tombolator;

import android.app.Application;
import androidx.room.Room;

public class TomboDbApplication extends Application {

    TomboDb tomboDb;

    @Override
    public void onCreate() {

        super.onCreate();

        tomboDb = Room.databaseBuilder(this, TomboDb.class, TomboDb.NAME)
                .fallbackToDestructiveMigration().build();
    }

    public TomboDb getTomboDb() {
        return tomboDb;
    }
}