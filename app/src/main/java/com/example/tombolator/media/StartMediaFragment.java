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
import com.example.tombolator.DatabaseApplication;
import com.example.tombolator.R;

import java.util.ArrayList;
import java.util.List;

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

    /* TODO: Helper for development. Will be replaced later. */
    private Button deleteAllButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        layout = inflater.inflate(R.layout.fragment_start_media, container, false);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.buttonBack);
        newMediaButton = layout.findViewById(R.id.button_new_media);
        deleteAllButton = layout.findViewById(R.id.buttonDeleteAll);

        registerObserver();
        registerOnClickListener();

        refreshView();

        return layout;
    }

    private void registerObserver() {
        mediaViewModel.getMediaDatabase().observe(this.getActivity(), new MediaInsertedObserver());
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

        deleteAllButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        DatabaseApplication context = ((DatabaseApplication) getActivity().getApplicationContext());
                        final MediaDao mediaDao = context.getMediaDatabase().mediaDao();
                        mediaDao.nukeTable();

                        mediaViewModel.removeAllMedia();
                    }
                });
            }
        });
    }

    public void refreshView() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                DatabaseApplication context = ((DatabaseApplication) getActivity().getApplicationContext());
                final MediaDao mediaDao = context.getMediaDatabase().mediaDao();
                List<Integer> mediaIds = mediaDao.getAllIds();
                List<Media> mediaList = new ArrayList<>();

                for (int id : mediaIds) {
                    mediaList.add(mediaDao.getById(id));
                }

                mediaViewModel.addMedia(mediaList);
            }
        });
    }

    private class MediaInsertedObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaListInserted) {

            linearLayoutMedia.removeAllViews();

            for(Media media : mediaListInserted) {

                int id = media.getId();
                String name = media.getName();
                String title = media.getTitle();
                int number = media.getNumber();
                String type = media.getType();

                String mediaString = "[" + id + "] " + type + ": " + name + " - " + title + " (" + number + ")";

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setTypeface(backButton.getTypeface());
                textView.setTextSize(16);
                textView.setText(mediaString);

                linearLayoutMedia.addView(textView);
            }
        }
    }
}