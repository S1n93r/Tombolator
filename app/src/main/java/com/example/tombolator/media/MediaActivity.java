package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    private MediaMainFragment mediaMainFragment;
    private MediaCreationStepOneTypesFragment mediaCreationStepOneTypesFragment;
    private MediaCreationStepTwoDescriptionsFragment mediaCreationStepTwoDescriptionsFragment;
    private MediaDetailsFragment mediaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mediaMainFragment = MediaMainFragment.newInstance();

        mediaDetailsFragment = MediaDetailsFragment.newInstance();
        mediaCreationStepOneTypesFragment = MediaCreationStepOneTypesFragment.newInstance();
        mediaCreationStepTwoDescriptionsFragment = MediaCreationStepTwoDescriptionsFragment.newInstance();
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