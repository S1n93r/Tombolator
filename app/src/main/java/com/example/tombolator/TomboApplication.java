package com.example.tombolator;

import android.app.Application;

public class TomboApplication extends Application {

    TomboDatabase tomboDatabase;

    @Override
    public void onCreate() {

        super.onCreate();

        tomboDatabase = TomboDatabase.getInstance(getApplicationContext());
    }

    public TomboDatabase getTomboDb() {
        return tomboDatabase;
    }
}