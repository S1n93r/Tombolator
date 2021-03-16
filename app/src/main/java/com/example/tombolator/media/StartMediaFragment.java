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
import java.util.Objects;

public class StartMediaFragment extends Fragment {

    private View layout;
    private final MediaActivity parent;

    public static StartMediaFragment newInstance(MediaActivity parent) {
        return new StartMediaFragment(parent);
    }

    private StartMediaFragment(MediaActivity parent) {
        this.parent = parent;
    }

    private MediaActivityViewModel mediaViewModel;

    private LinearLayout linearLayoutMedia;

    private Button backButton;
    private Button newMediaButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        layout = inflater.inflate(R.layout.fragment_start_media, container, false);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.buttonBack);
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

            for(Media media : Objects.requireNonNull(mediaViewModel.getMediaDatabase().getValue())){

                int id = media.getId();
                String name = media.getName();
                String title = media.getTitle();
                int number = media.getNumber();
                String type = media.getType();

                String mediaString = "[" + id + "] " + type + ": " + name + " - " + title + " (" + number + ")";

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setText(mediaString);

                linearLayoutMedia.addView(textView);
            }
        }
    }
}