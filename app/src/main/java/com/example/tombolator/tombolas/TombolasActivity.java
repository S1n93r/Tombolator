package com.example.tombolator.tombolas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private CreateTombolaFragment createTombolaFragment;
    private TombolaDetailsFragment tombolaDetailsFragment;
    private CreateMediaFragment createMediaFragment;
    private ChooseMediaTypeFragment chooseMediaTypeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance();
        createTombolaFragment = CreateTombolaFragment.newInstance();
        tombolaDetailsFragment = TombolaDetailsFragment.newInstance();
        createMediaFragment = CreateMediaFragment.newInstance();
        chooseMediaTypeFragment = ChooseMediaTypeFragment.newInstance();

        setContentView(R.layout.tombolas_activity);
        if (savedInstanceState == null) {
            switchToTombolaList();
        }
    }

    protected void switchToTombolaList() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaMainFragment)
                .commitNow();
    }

    protected void switchToCreateTombola() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createTombolaFragment)
                .commitNow();
    }

    protected void switchToTombolaDetails() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaDetailsFragment)
                .commitNow();
    }

    protected void switchToCreateMedia() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createMediaFragment)
                .commitNow();
    }

    protected void switchToChooseMedia() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, chooseMediaTypeFragment)
                .commitNow();
    }
}