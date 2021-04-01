package com.example.tombolator.media;

import android.os.AsyncTask;
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
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaCreateNewFragment extends Fragment {

    MediaActivity parent;

    public static MediaCreateNewFragment newInstance(MediaActivity parent) {
        return new MediaCreateNewFragment(parent);
    }

    private MediaCreateNewFragment(MediaActivity parent) {
        this.parent = parent;
    }

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

        View layout = inflater.inflate(R.layout.media_creation_fragment, container, false);

        editTextName = layout.findViewById(R.id.edit_text_name);
        editTextTitle = layout.findViewById(R.id.editTextTitle);
        editTextNumber = layout.findViewById(R.id.number_label);
        spinnerType = layout.findViewById(R.id.type_label);

        saveButton = layout.findViewById(R.id.button_save);
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

                    final Media media = new Media(name, title, number, type);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                    .getApplicationContext());

                            final MediaDao mediaDao = context.getTomboDb().mediaDao();
                            mediaDao.insertMedia(media);
                        }
                    });

                    resetForm();
                    parent.switchToMainView();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
                parent.switchToMainView();
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