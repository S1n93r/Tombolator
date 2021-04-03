package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    private MediaMainFragment mediaMainFragment;
    private MediaCreateNewFragment mediaCreateNewFragment;
    private MediaDetailsFragment mediaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mediaMainFragment = MediaMainFragment.newInstance();
        mediaCreateNewFragment = MediaCreateNewFragment.newInstance();
        mediaDetailsFragment = MediaDetailsFragment.newInstance();

        setContentView(R.layout.media_activity);
        if (savedInstanceState == null) {
            switchToMainView();
        }
    }

    protected void switchToMainView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaMainFragment)
                .commitNow();
    }

    protected void switchToCreateMediaView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaCreateNewFragment)
                .commitNow();
    }

    protected void switchToMediaDetailsView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaDetailsFragment)
                .commitNow();
    }
}