package com.example.tombolator.media;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

import java.util.Objects;

public class MediaDetailsFragment extends Fragment {

    MediaActivity parent;

    public static MediaDetailsFragment newInstance(MediaActivity parent) {
        return new MediaDetailsFragment(parent);
    }

    private MediaDetailsFragment(MediaActivity parent) {
        this.parent = parent;
    }

    private MediaActivityViewModel mediaActivityViewModel;

    private TextView idValue;
    private TextView nameValue;
    private TextView numberValue;
    private TextView titleValue;
    private TextView typeValue;
    private TextView createdAt;

    private Button backButton;
    private Button deleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_media_details, container, false);

        idValue = layout.findViewById(R.id.id_value);
        nameValue = layout.findViewById(R.id.name_value);
        numberValue = layout.findViewById(R.id.number_value);
        titleValue = layout.findViewById(R.id.title_value);
        typeValue = layout.findViewById(R.id.type_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        backButton = layout.findViewById(R.id.button_back);
        deleteButton = layout.findViewById(R.id.button_delete);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getSelectedMedia().observe(Objects.requireNonNull(this.getActivity()), new SelectedMediaObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToMainView();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Media media = mediaActivityViewModel.getSelectedMedia().getValue();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        final MediaDao mediaDao = context.getTomboDb().mediaDao();
                        mediaDao.deleteMedia(media);
                    }
                });

                mediaActivityViewModel.removeMedia(Objects.requireNonNull(media).getId());

                parent.switchToMainView();
            }
        });
    }

    private class SelectedMediaObserver implements Observer<Media> {

        @Override
        public void onChanged(Media media) {

            idValue.setText(String.valueOf(media.getId()));
            nameValue.setText(media.getName());
            numberValue.setText(String.valueOf(media.getNumber()));
            titleValue.setText(media.getTitle());
            typeValue.setText(media.getType());
            createdAt.setText(String.valueOf(media.getCreationTimestamp()));
        }
    }
}