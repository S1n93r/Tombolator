package com.example.tombolator.media;

import android.os.AsyncTask;
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
import com.example.tombolator.TomboDbApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MediaMainFragment extends Fragment {

    private final MediaActivity parent;

    public static MediaMainFragment newInstance(MediaActivity parent) {
        return new MediaMainFragment(parent);
    }

    private MediaMainFragment(MediaActivity parent) {
        this.parent = parent;
    }

    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private MediaActivityViewModel mediaActivityViewModel;

    private LinearLayout linearLayoutMedia;

    private Button backButton;
    private Button newMediaButton;

    /* TODO: Helper for development. Will be replaced later. */
    private Button deleteAllButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_media_start, container, false);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.button_back);
        newMediaButton = layout.findViewById(R.id.button_new_media);
        deleteAllButton = layout.findViewById(R.id.buttonDeleteAll);

        registerObserver();
        registerOnClickListener();

        refreshView();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getMediaDatabase()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaInsertedObserver());
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
                parent.switchToCreateMediaView();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        final MediaDao mediaDao = context.getTomboDb().mediaDao();
                        mediaDao.nukeTable();

                        mediaActivityViewModel.removeAllMedia();
                    }
                });
            }
        });
    }

    public void refreshView() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                        .getApplicationContext());

                final MediaDao mediaDao = context.getTomboDb().mediaDao();
                List<Long> mediaIds = mediaDao.getAllIds();
                List<Media> mediaList = new ArrayList<>();

                for (long id : mediaIds) {
                    mediaList.add(mediaDao.getById(id));
                }

                mediaActivityViewModel.addMedia(mediaList);
            }
        });
    }

    private class MediaInsertedObserver implements Observer<Map<Long, Media>> {

        @Override
        public void onChanged(Map<Long, Media> mediaMapInserted) {

            linearLayoutMedia.removeAllViews();

            for (Map.Entry<Long, Media> pair : mediaMapInserted.entrySet()) {

                Media media = pair.getValue();

                long id = media.getId();
                String name = media.getName();
                String title = media.getTitle();
                int number = media.getNumber();
                String type = media.getType();

                String mediaString = "[" + id + "] " + type + ": " + name + " - " + title + " (" + number + ")";

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setTypeface(getResources().getFont(R.font.comic_sans_ms));
                textView.setTextSize(20);
                textView.setText(mediaString);
                textView.setOnClickListener(showDetailsListener);
                textView.setId((int) id);

                linearLayoutMedia.addView(textView);
            }
        }
    }

    private class ShowDetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long mediaId = textView.getId();
            mediaActivityViewModel.selectMedia(mediaId);

            parent.switchToMediaDetailsView();
        }
    }
}