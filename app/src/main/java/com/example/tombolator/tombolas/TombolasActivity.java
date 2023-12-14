package com.example.tombolator.tombolas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private CreateTombolaFragment createTombolaFragment;
    private TombolaDetailsFragment tombolaDetailsFragment;
    private CreateMediaFragment createMediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance();
        createTombolaFragment = CreateTombolaFragment.newInstance();
        tombolaDetailsFragment = TombolaDetailsFragment.newInstance();
        createMediaFragment = CreateMediaFragment.newInstance();

        setContentView(R.layout.tombolas_activity);
        if (savedInstanceState == null) {
            switchToTombolasMainView();
        }
    }

    protected void switchToTombolasMainView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaMainFragment)
                .commitNow();
    }

    protected void switchToCreateTombolaView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createTombolaFragment)
                .commitNow();
    }

    protected void switchToTombolaDetailsView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaDetailsFragment)
                .commitNow();
    }

    protected void switchToMediaCreationView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, createMediaFragment)
                .commitNow();
    }
}