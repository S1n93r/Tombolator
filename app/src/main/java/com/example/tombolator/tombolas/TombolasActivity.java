package com.example.tombolator.tombolas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private CreateTombolaFragment createTombolaFragment;
    private TombolaDetailsFragment tombolaDetailsFragment;
    private ChooseMediaTypeFragment chooseMediaTypeFragment;
    private CreateBookFragment createBookFragment;
    private CreateCassetteFragment createCassetteFragment;
    private CreateMovieFragment createMovieFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance();
        createTombolaFragment = CreateTombolaFragment.newInstance();
        tombolaDetailsFragment = TombolaDetailsFragment.newInstance();
        chooseMediaTypeFragment = ChooseMediaTypeFragment.newInstance();
        createBookFragment = CreateBookFragment.newInstance();
        createCassetteFragment = CreateCassetteFragment.newInstance();
        createMovieFragment = CreateMovieFragment.newInstance();

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

    protected void switchToChooseMedia() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, chooseMediaTypeFragment)
                .commitNow();
    }

    protected void switchToCreateBook() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createBookFragment)
                .commitNow();
    }

    protected void switchToCreateCassette() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createCassetteFragment)
                .commitNow();
    }

    protected void switchToCreateMovie() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createMovieFragment)
                .commitNow();
    }
}