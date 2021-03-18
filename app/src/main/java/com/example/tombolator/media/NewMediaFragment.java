package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.ArrayList;
import java.util.List;

public class NewMediaFragment extends Fragment {

    MediaActivity parent;

    public static NewMediaFragment newInstance(MediaActivity parent) {
        return new NewMediaFragment(parent);
    }

    private NewMediaFragment(MediaActivity parent) {
        this.parent = parent;
    }

    private View layout;
    private MediaActivityViewModel mediaViewModel;

    private TextView editTextName;
    private TextView editTextTitle;
    private TextView editTextNumber;
    private Spinner spinnerType;

    private Button saveButton;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        layout = inflater.inflate(R.layout.fragment_new_media, container, false);

        editTextName = layout.findViewById(R.id.editTextName);
        editTextTitle = layout.findViewById(R.id.editTextTitle);
        editTextNumber = layout.findViewById(R.id.editTextNumber);
        spinnerType = layout.findViewById(R.id.spinnerType);

        saveButton = layout.findViewById(R.id.buttonSave);
        backButton = layout.findViewById(R.id.buttonBack);

        setUpMediaTypeSpinner();
        registerOnClickListener();

        return layout;
    }

    private void setUpMediaTypeSpinner() {

        List<String> mediaTypesForSpinner = new ArrayList<>();
        mediaTypesForSpinner.add("Hörspiel");
        mediaTypesForSpinner.add("Hörbuch");
        mediaTypesForSpinner.add("Film");
        mediaTypesForSpinner.add("Serien");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), android.R.layout.simple_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerType.setAdapter(arrayAdapter);
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextName.getText().length() > 0){

                    String name = editTextName.getText() != null ? editTextName.getText().toString() : "";
                    String title = editTextTitle.getText() != null ? editTextTitle.getText().toString() : "";
                    String numberAsString = editTextNumber.getText() != null ? editTextNumber.getText().toString() : "";
                    String type = spinnerType.getSelectedItem() != null ? spinnerType.getSelectedItem().toString() : "";

                    int number = numberAsString.length() > 0 ? Integer.parseInt(numberAsString) : -1;

                    Media media = new Media(name, title, number, type);

                    mediaViewModel.addMedia(media);

                    resetForm();

                    parent.switchToStartView();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToStartView();
            }
        });
    }

    private void resetForm() {

        editTextName.setText("");
        editTextTitle.setText("");
        editTextNumber.setText("");
        spinnerType.setSelection(0);
    }
}