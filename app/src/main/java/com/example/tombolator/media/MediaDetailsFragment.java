package com.example.tombolator.media;

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
import com.example.tombolator.tombolas.TombolasActivityViewModel;
import com.example.tombolator.utils.DateUtil;

public class MediaDetailsFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private TextView idValue;
    private TextView nameValue;
    private TextView numberValue;
    private TextView titleValue;
    private TextView typeValue;
    private TextView createdAt;

    private Button editMediaButton;
    private Button backButton;
    private Button deleteButton;

    private MediaDetailsFragment(){}

    public static MediaDetailsFragment newInstance() {
        return new MediaDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_details_fragment, container, false);

        idValue = layout.findViewById(R.id.id_value);
        nameValue = layout.findViewById(R.id.name_value);
        numberValue = layout.findViewById(R.id.number_value);
        titleValue = layout.findViewById(R.id.title_value);
        typeValue = layout.findViewById(R.id.type_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        editMediaButton = layout.findViewById(R.id.button_edit_media);
        backButton = layout.findViewById(R.id.back_button);
        deleteButton = layout.findViewById(R.id.button_delete);

        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getSelectedMedia().observe(this, new SelectedMediaObserver());
    }

    private void registerOnClickListener() {

        editMediaButton.setOnClickListener((View v) -> mediaActivity.switchToCreationStepOne());

        backButton.setOnClickListener((View v) -> mediaActivity.switchToMediaListStepTwo());

        deleteButton.setOnClickListener((View v) -> deleteMedia());
    }

    private void deleteMedia() {

        Media media = mediaActivityViewModel.getSelectedMedia().getValue();

        if(media == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        tombolasActivityViewModel.deleteMediaFromAllTombolas(media);
        mediaActivityViewModel.delete(media);

        mediaActivity.switchToMediaListStepTwo();
    }

    private class SelectedMediaObserver implements Observer<Media> {

        @Override
        public void onChanged(Media media) {

            idValue.setText(String.valueOf(media.getId()));
            nameValue.setText(media.getName());
            numberValue.setText(String.valueOf(media.getNumber()));
            titleValue.setText(media.getTitle());
            typeValue.setText(media.getMediaType());
            createdAt.setText(DateUtil.formatDate(media.getCreationTimestamp()));
        }
    }
}