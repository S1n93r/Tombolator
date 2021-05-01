package com.example.tombolator.tombolas;

import android.os.AsyncTask;
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
import com.example.tombolator.TomboDbApplication;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.media.MediaDao;

import java.util.List;
import java.util.Map;
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

        refreshViewModel();

        return layout;
    }

    private void registerObserver() {
            mediaActivityViewModel.getMediaDatabase()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaInsertedObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(v -> {
            resetForm();
            tombolasActivity.switchToCreationStepOne();
        });

        saveButton.setOnClickListener(v -> {

            final Tombola tombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            AsyncTask.execute(() -> {

                TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                        .getApplicationContext());

                final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                tombolaDao.insertTombola(tombola);
            });

            resetForm();
            tombolasActivity.switchToTombolasMainView();
        });
    }

    private void resetForm() {
        availableMedia.removeAllViews();
        addedMedia.removeAllViews();
    }

    public void refreshViewModel() {

        AsyncTask.execute(() -> {

            TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                    .getApplicationContext());

            final MediaDao mediaDao = context.getTomboDb().mediaDao();
            List<Media> mediaList = mediaDao.getAllMedia();

            mediaActivityViewModel.clearAndAddMedia(mediaList);
        });
    }

    private class MediaInsertedObserver implements Observer<Map<Long, Media>> {

        @Override
        public void onChanged(Map<Long, Media> mediaMapInserted) {

            availableMedia.removeAllViews();
            addedMedia.removeAllViews();

            for (Map.Entry<Long, Media> pair : mediaMapInserted.entrySet()) {

                Media media = pair.getValue();

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

            Tombola tombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            if(mediaActivityViewModel.getMediaDatabase().getValue() == null) {
                /* TODO: Write to log. */
                return;
            }

            if(tombola == null) {
                /* TODO: Write to log. */
                return;
            }

            TextView textView = (TextView) view;
            long mediaId = view.getId();
            Media media = mediaActivityViewModel.getMediaDatabase().getValue().get(mediaId);

            if(media == null) {
                /* TODO: Write to log. */
                return;
            }

            if(textView.getParent() == availableMedia) {

                tombola.addMedia(media);

                availableMedia.removeView(textView);
                addedMedia.addView(textView);
            } else {

                tombola.removeMedia(media);

                addedMedia.removeView(textView);
                availableMedia.addView(textView);
            }
        }
    }
}