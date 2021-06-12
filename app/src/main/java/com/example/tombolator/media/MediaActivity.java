package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    private MediaListStepOneMediaTypeSelectFragment mediaListStepOneMediaTypeSelectFragment;
    private MediaListStepTwoMediaListFragment mediaListStepTwoMediaListFragment;
    private MediaCreationStepOneTypesFragment mediaCreationStepOneTypesFragment;
    private MediaCreationStepTwoDescriptionsFragment mediaCreationStepTwoDescriptionsFragment;
    private MediaDetailsFragment mediaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mediaListStepOneMediaTypeSelectFragment = MediaListStepOneMediaTypeSelectFragment.newInstance();
        mediaListStepTwoMediaListFragment = MediaListStepTwoMediaListFragment.newInstance();

        mediaDetailsFragment = MediaDetailsFragment.newInstance();
        mediaCreationStepOneTypesFragment = MediaCreationStepOneTypesFragment.newInstance();
        mediaCreationStepTwoDescriptionsFragment = MediaCreationStepTwoDescriptionsFragment.newInstance();
        setContentView(R.layout.media_activity);
        if (savedInstanceState == null) {
            switchToMediaListStepTwo();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /* TODO: Save media list from model to database. */
    }

    protected void switchToMediaListStepOne() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaListStepOneMediaTypeSelectFragment)
                .commitNow();
    }

    protected void switchToMediaListStepTwo() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaListStepTwoMediaListFragment)
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