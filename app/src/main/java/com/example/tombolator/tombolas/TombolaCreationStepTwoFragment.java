package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;

import java.util.List;
import java.util.Objects;

public class TombolaCreationStepTwoFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel tombolasActivityViewModel;
    private MediaActivityViewModel mediaActivityViewModel;

    private LinearLayout availableMedia;
    private LinearLayout addedMedia;

    private Button backButton;
    private Button saveButton;

    private TombolaCreationStepTwoFragment() {}

    public static TombolaCreationStepTwoFragment newInstance() {
        return new TombolaCreationStepTwoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombolas_creation_step_two_fragment, container, false);

        availableMedia = layout.findViewById(R.id.linear_layout_available_media);
        addedMedia = layout.findViewById(R.id.linear_layout_added_media);

        backButton = layout.findViewById(R.id.button_back);
        saveButton = layout.findViewById(R.id.button_save);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
            mediaActivityViewModel.getAllMediaLiveData()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaListObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> {
            resetForm();
            tombolasActivity.switchToCreationStepOne();
        });

        saveButton.setOnClickListener(new SaveTombolaListener());
    }

    private class SaveTombolaListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(tombolasActivityViewModel.getSelectedTombola().getValue() == null) {
                /* TODO: Add log entry */
                return;
            }

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();
            addMediaToTombola(selectedTombola);

            tombolasActivityViewModel.insertTombola(selectedTombola);

            resetForm();
            tombolasActivity.switchToTombolasMainView();
        }
    }

    private void addMediaToTombola(Tombola tombola) {

        tombola.getMediaDrawn().clear();
        tombola.getMediaAvailable().clear();

        for(int i=0; i<addedMedia.getChildCount(); i++) {

            TextView view = (TextView) addedMedia.getChildAt(i);
            long mediaId = view.getId();

            Media media = mediaActivityViewModel.getMedia(mediaId);

            if(media == null) {
                /* Add log entry here. */
                continue;
            }

            tombola.addMedia(media);
        }
    }

    private void resetForm() {
        availableMedia.removeAllViews();
        addedMedia.removeAllViews();
    }

    private class MediaListObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            availableMedia.removeAllViews();
            addedMedia.removeAllViews();

            for (Media media : mediaList) {

                long id = media.getId();

                TextView textView = (TextView) View.inflate(
                        tombolasActivity.getApplicationContext(), R.layout.list_element, null);

                textView.setText(media.toLabel());
                textView.setOnClickListener(new SwitchMediaBetweenAvailableAndAdded());
                textView.setId((int) id);

                Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

                if(selectedTombola != null) {

                    List<Media> mediaListSelectedTombola = selectedTombola.getAllMedia();

                    for(Media mediaSelectedTombola : mediaListSelectedTombola) {

                        if(mediaSelectedTombola.getId().equals(media.getId())) {

                            addedMedia.addView(textView);
                            break;
                        }
                    }

                    if(textView.getParent() == null)
                        availableMedia.addView(textView);
                }
            }
        }
    }

    private class SwitchMediaBetweenAvailableAndAdded implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;

            if(textView.getParent() == availableMedia) {

                availableMedia.removeView(textView);
                addedMedia.addView(textView);
            } else {

                addedMedia.removeView(textView);
                availableMedia.addView(textView);
            }
        }
    }
}