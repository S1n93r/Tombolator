package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class MediaActivity extends AppCompatActivity {

    private StartMediaFragment startMediaFragment;
    private NewMediaFragment newMediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startMediaFragment = StartMediaFragment.newInstance(this);
        newMediaFragment = NewMediaFragment.newInstance(this);

        setContentView(R.layout.activity_media);
        if (savedInstanceState == null) {
            switchToStartView();
        }
    }

    protected void switchToStartView() {

        startMediaFragment.setArguments(newMediaFragment.getArguments());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, startMediaFragment)
                .commitNow();
    }

    protected void switchToNewMediaView() {

        newMediaFragment.setArguments(startMediaFragment.getArguments());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newMediaFragment)
                .commitNow();
    }
}