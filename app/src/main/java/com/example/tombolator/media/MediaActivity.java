package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    Bundle savedInstanceState;
    private MediaListFragment mediaListFragment;
    private MediaListFragment2 mediaListFragment2;
    private MediaCreationStepOneTypesFragment mediaCreationStepOneTypesFragment;
    private MediaCreationStepTwoDescriptionsFragment mediaCreationStepTwoDescriptionsFragment;
    private MediaDetailsFragment mediaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mediaListFragment = MediaListFragment.newInstance();
        mediaListFragment2 = MediaListFragment2.newInstance();

        mediaDetailsFragment = MediaDetailsFragment.newInstance();
        mediaCreationStepOneTypesFragment = MediaCreationStepOneTypesFragment.newInstance();
        mediaCreationStepTwoDescriptionsFragment = MediaCreationStepTwoDescriptionsFragment.newInstance();
        setContentView(R.layout.media_activity);

        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected void onStart() {

        super.onStart();

        if (savedInstanceState == null) {
            switchToMediaListStepTwo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /* TODO: Save media list from model to database. */
    }

    protected void switchToMediaListStepTwo() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaListFragment2)
                .commitNow();
    }

    protected void switchToCreationStepOne() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaCreationStepOneTypesFragment)
                .commitNow();
    }

    protected void switchToCreationStepTwo() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaCreationStepTwoDescriptionsFragment)
                .commitNow();
    }

    protected void switchToMediaDetailsView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaDetailsFragment)
                .commitNow();
    }
}