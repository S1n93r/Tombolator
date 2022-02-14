package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    Bundle savedInstanceState;

    private MediaListFragment mediaListFragment;
    private MediaCreationStepOneTypesFragment mediaCreationStepOneTypesFragment;
    private MediaCreationStepTwoDescriptionsFragment mediaCreationStepTwoDescriptionsFragment;
    private MediaDetailsFragment mediaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mediaListFragment = MediaListFragment.newInstance();

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
            switchToMediaList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /* TODO: Save media list from model to database. */
    }

    protected void switchToView(Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }

    protected void switchToMediaList() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaListFragment)
                .commitNow();
    }

    protected void switchToCreationStepOne(Fragment fragmentBefore) {

        /* If the "fragment before" would be step two, because you returned from it, the reference is not overridden.
         This way the original "before" like media details or creation steps remains intact. */
        if (!fragmentBefore.equals(mediaCreationStepTwoDescriptionsFragment))
            mediaCreationStepOneTypesFragment.setFragmentBefore(fragmentBefore);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaCreationStepOneTypesFragment)
                .commitNow();
    }

    protected void switchToCreationStepTwo() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaCreationStepTwoDescriptionsFragment)
                .commitNow();
    }

    protected void switchToMediaDetailsView(Fragment fragmentBefore) {

        mediaDetailsFragment.setFragmentBefore(fragmentBefore);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mediaDetailsFragment)
                .commitNow();
    }
}