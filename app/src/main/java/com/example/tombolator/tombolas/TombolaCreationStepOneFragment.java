package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TombolaCreationStepOneFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private EditText nameEditText;

    private Button continueButton;
    private Button backButton;

    private TombolaCreationStepOneFragment() {}

    public static TombolaCreationStepOneFragment newInstance() {
        return new TombolaCreationStepOneFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombola_creation_step_one_fragment, container, false);

        nameEditText = layout.findViewById(R.id.edit_text_name);

        continueButton = layout.findViewById(R.id.button_continue);
        backButton = layout.findViewById(R.id.button_back);

        setUpMediaTypeSpinner();
        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void registerObserver() {
        tombolasActivityViewModel.getSelectedTombola().observe(
                Objects.requireNonNull(getActivity()), tombola -> nameEditText.setText(tombola.getName()));
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
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(v -> {
            resetForm();
            tombolasActivity.switchToTombolasMainView();
        });

        continueButton.setOnClickListener(v -> {

            String name = nameEditText.getText() != null ? nameEditText.getText().toString() : "";

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();
            Objects.requireNonNull(selectedTombola).setName(name);

            tombolasActivity.switchToCreationStepTwo();
        });
    }

    private void resetForm() {
        nameEditText.getText().clear();
    }
}