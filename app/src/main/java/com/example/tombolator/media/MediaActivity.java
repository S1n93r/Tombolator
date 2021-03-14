package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    private MediaActivityViewModel mediaActivityViewModel;

    private StartMediaFragment startMediaFragment;
    private NewMediaFragment newMediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        startMediaFragment = StartMediaFragment.newInstance(this);
        newMediaFragment = NewMediaFragment.newInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, startMediaFragment)
                    .commitNow();
        }

        mediaActivityViewModel = new ViewModelProvider(this).get(MediaActivityViewModel.class);
    }

    protected void switchToStartView() {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, startMediaFragment)
                    .commitNow();
    }

    protected void switchToNewMediaView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newMediaFragment)
                .commitNow();
    }
}