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

public class TombolaCreationFragmentStepOne extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private EditText nameEditText;

    private Button continueButton;
    private Button backButton;

    private TombolaCreationFragmentStepOne() {}

    public static TombolaCreationFragmentStepOne newInstance() {
        return new TombolaCreationFragmentStepOne();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombolas_creation_fragment_step_one, container, false);

        nameEditText = layout.findViewById(R.id.edit_text_name);

        backButton = layout.findViewById(R.id.back_button);
        continueButton = layout.findViewById(R.id.continue_button);

        setUpMediaTypeSpinner();
        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void registerObserver() {
        tombolasActivityViewModel.getSelectedTombola().observe(this,
                (Tombola tombola) -> nameEditText.setText(tombola.getName()));
    }

    private void setUpMediaTypeSpinner() {

        List<String> mediaTypesForSpinner = new ArrayList<>();
        mediaTypesForSpinner.add(Media.MediaType.CASSETTE);
        mediaTypesForSpinner.add(Media.MediaType.CD);
        mediaTypesForSpinner.add(Media.MediaType.BOOK);
        mediaTypesForSpinner.add(Media.MediaType.E_BOOK);
        mediaTypesForSpinner.add(Media.MediaType.DVD);
        mediaTypesForSpinner.add(Media.MediaType.BLU_RAY);

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

            if(tombolasActivityViewModel.getSelectedTombola().getValue() == null) {
                /* TODO: Create log entry... Like a nice carving. */
                throw new NullPointerException();
            }

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            /* TODO: Type should be asked by spinner. */
            selectedTombola.setType(Tombola.Type.REUSE);
            selectedTombola.setName(nameEditText.getText().toString());

            tombolasActivity.switchToCreationStepTwo();
        });
    }

    private void resetForm() {
        nameEditText.getText().clear();
    }
}