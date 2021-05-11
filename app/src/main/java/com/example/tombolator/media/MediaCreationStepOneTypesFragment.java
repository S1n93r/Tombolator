package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.ArrayList;
import java.util.List;

public class MediaCreationStepOneTypesFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private Spinner mediaTypeSpinner;

    private Button continueButton;
    private Button backButton;

    private MediaCreationStepOneTypesFragment() {}

    public static MediaCreationStepOneTypesFragment newInstance() {
        return new MediaCreationStepOneTypesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_creation_step_one_types_fragment, container, false);

        mediaTypeSpinner = layout.findViewById(R.id.spinner_media_types);

        continueButton = layout.findViewById(R.id.button_continue);
        backButton = layout.findViewById(R.id.button_back);

        setUpMediaTypeSpinner();
        registerOnClickListener();

        return layout;
    }

    private void setUpMediaTypeSpinner() {

        List<String> mediaTypesForSpinner = new ArrayList<>();
        mediaTypesForSpinner.add(Media.Type.CASSETTE);
        mediaTypesForSpinner.add(Media.Type.CD);
        mediaTypesForSpinner.add(Media.Type.BOOK);
        mediaTypesForSpinner.add(Media.Type.E_BOOK);
        mediaTypesForSpinner.add(Media.Type.DVD);
        mediaTypesForSpinner.add(Media.Type.BLU_RAY);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);

        mediaTypeSpinner.setAdapter(arrayAdapter);
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(v -> {
            resetForm();
            mediaActivity.switchToMediaListStepTwo();
        });

        continueButton.setOnClickListener(v -> {

            String type = mediaTypeSpinner.getSelectedItem() != null ? mediaTypeSpinner.getSelectedItem().toString() : "";

            if(mediaActivityViewModel.getSelectedMedia().getValue() == null) {
                /* TODO: Add log entry. */
                return;
            }

            mediaActivityViewModel.getSelectedMedia().getValue().setType(type);

            resetForm();
            mediaActivity.switchToCreationStepTwo();
        });
    }

    private void resetForm() {

        mediaTypeSpinner.setSelection(0);
    }
}