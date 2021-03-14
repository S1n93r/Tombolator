package com.example.tombolator.media;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {

    private MediaActivityViewModel mediaActivityViewModel;

    private StartMediaFragment startMediaFragment;
    private NewMediaFragment newMediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mediaActivityViewModel = new ViewModelProvider(this).get(MediaActivityViewModel.class);

        startMediaFragment = StartMediaFragment.newInstance(this);
        newMediaFragment = NewMediaFragment.newInstance(this);

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(
                "mediaDatabase", (ArrayList) mediaActivityViewModel.getMediaDatabase().getValue());

        startMediaFragment.setArguments(bundle);
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