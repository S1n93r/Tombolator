package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.List;

public class StartMediaFragment extends Fragment {

    private View layout;
    private MediaActivity parent;

    public static StartMediaFragment newInstance(MediaActivity parent) {
        return new StartMediaFragment(parent);
    }

    private StartMediaFragment(MediaActivity parent) {
        this.parent = parent;
    };

    private MediaActivityViewModel mediaViewModel;

    private LinearLayout linearLayoutMedia;

    private Button backButton;
    private Button newMediaButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaViewModel = new ViewModelProvider(this).get(MediaActivityViewModel.class);

        layout = inflater.inflate(R.layout.fragment_start_media, container, false);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.button_back);
        newMediaButton = layout.findViewById(R.id.button_new_media);

        dataBind();
        registerOnClickListener();

        return layout;
    }

    private void dataBind() {

        mediaViewModel.getMediaDatabase().observe(getViewLifecycleOwner(), new MediaListObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.finish();
            }
        });

        newMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.switchToNewMediaView();
            }
        });
    }

    private class MediaListObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            linearLayoutMedia.removeAllViews();

            for(Media media : mediaList){

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setText(media.toString());
                linearLayoutMedia.addView(textView);
            }
        }
    }
}