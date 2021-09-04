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

public class TombolaCreationFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private EditText nameEditText;

    private Button saveButton;
    private Button backButton;

    private TombolaCreationFragment() {}

    public static TombolaCreationFragment newInstance() {
        return new TombolaCreationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombolas_creation_fragment, container, false);

        nameEditText = layout.findViewById(R.id.edit_text_name);

        backButton = layout.findViewById(R.id.button_back);
        saveButton = layout.findViewById(R.id.button_save);

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

        saveButton.setOnClickListener(new SaveTombolaListener());
    }

    private class SaveTombolaListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(tombolasActivityViewModel.getSelectedTombola().getValue() == null) {
                /* TODO: Add log entry */
                throw new NullPointerException();
            }

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            String name = nameEditText.getText() != null ? nameEditText.getText().toString() : "";
            selectedTombola.setName(name);

            selectedTombola.setType(Tombola.Type.REUSE);

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolasMainView();
        }
    }

    private void resetForm() {
        nameEditText.getText().clear();
    }
}