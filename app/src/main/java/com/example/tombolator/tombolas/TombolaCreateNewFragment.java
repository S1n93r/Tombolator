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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TombolaCreateNewFragment extends Fragment {

    TombolasActivity parent;

    public static TombolaCreateNewFragment newInstance(TombolasActivity parent) {
        return new TombolaCreateNewFragment(parent);
    }

    private TombolaCreateNewFragment(TombolasActivity parent) {
        this.parent = parent;
    }

    private MediaActivityViewModel mediaActivityViewModel;

    private TextView editTextName;

    private LinearLayout availableMedia;
    private LinearLayout addedMedia;

    private Button saveButton;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_tombolas_new, container, false);

        editTextName = layout.findViewById(R.id.edit_text_name);

        availableMedia = layout.findViewById(R.id.linear_layout_available_media);
        addedMedia = layout.findViewById(R.id.linear_layout_added_media);

        saveButton = layout.findViewById(R.id.button_save);
        backButton = layout.findViewById(R.id.button_back);

        registerObserver();
        registerOnClickListener();

        refreshViewModel();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getMediaDatabase()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaInsertedObserver());
    }

    public void refreshViewModel() {

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

    private void registerOnClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextName.getText().length() > 0){

                    String name = editTextName.getText() != null ? editTextName.getText().toString() : "";

                    final Tombola tombola = new Tombola(name);

                    if(addedMedia.getChildCount() > 0) {

                        for(int i=0; i<addedMedia.getChildCount(); i++) {

                            TextView textView = (TextView) addedMedia.getChildAt(i);
                            long mediaId = textView.getId();

                            if(mediaActivityViewModel.getMediaDatabase().getValue() == null)
                                throw new NullPointerException();

                            Media media = mediaActivityViewModel.getMediaDatabase().getValue().get(mediaId);

                            tombola.addMedia(media);
                        }
                    }

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                    .getApplicationContext());

                            final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                            tombolaDao.insertTombola(tombola);
                        }
                    });

                    resetForm();
                    parent.switchToTombolasMainView();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetForm();
                parent.switchToTombolasMainView();
            }
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
                String name = media.getName();
                String title = media.getTitle();
                int number = media.getNumber();
                String type = media.getType();

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setTypeface(getResources().getFont(R.font.comic_sans_ms));
                textView.setTextSize(20);
                textView.setText(media.toLabel());
                textView.setOnClickListener(new AddMedia());
                textView.setId((int) id);

                availableMedia.addView(textView);
            }
        }
    }

    private class AddMedia implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            availableMedia.removeView(textView);
            addedMedia.addView(textView);
        }
    }

    private void resetForm() {
        editTextName.setText("");
    }
}