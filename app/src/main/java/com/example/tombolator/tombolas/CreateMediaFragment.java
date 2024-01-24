package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaType;

import java.util.Arrays;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class CreateMediaFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel viewModel;

    private EditText editTextName;
    private EditText editTextTitle;
    private EditText editTextNumber;
    private EditText editTextAuthor;

    private ImageView saveButton;
    private ImageView backButton;

    private CreateMediaFragment() {
    }

    public static CreateMediaFragment newInstance() {
        return new CreateMediaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        viewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.create_media_fragment, container, false);

        editTextName = layout.findViewById(R.id.edit_text_name);
        editTextTitle = layout.findViewById(R.id.edit_text_title);
        editTextNumber = layout.findViewById(R.id.edit_text_number);
        editTextAuthor = layout.findViewById(R.id.edit_text_author);

        saveButton = layout.findViewById(R.id.button_save);
        backButton = layout.findViewById(R.id.back_button);

        setUpMediaTypeSpinner();
        registerOnClickListener();

        return layout;
    }

    private void setUpMediaTypeSpinner() {

        List<String> mediaTypesForSpinner = StreamSupport.stream(Arrays.asList(MediaType.values()))
                .map(MediaType::getCleanName)
                .collect(Collectors.toList());

        mediaTypesForSpinner.remove(0);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.requireActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> tombolasActivity.switchToChooseMedia());

        saveButton.setOnClickListener(new SaveMediaListener());
    }

    private class SaveMediaListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (editTextName.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_name_empty, Toast.LENGTH_LONG).show();
                return;
            }

            Media selectedMedia = viewModel.getSelectedMedia().getValue();

            assert selectedMedia != null;

            String name = editTextName.getText() != null ? editTextName.getText().toString() : "";
            String title = editTextTitle.getText() != null ? editTextTitle.getText().toString() : "";
            String numberAsString = editTextNumber.getText() != null ? editTextNumber.getText().toString() : "";
            String author = editTextAuthor.getText() != null ? editTextAuthor.getText().toString() : "";

            int number = numberAsString.length() > 0 ? Integer.parseInt(numberAsString) : -1;

            selectedMedia.setName(name);
            selectedMedia.setTitle(title);
            selectedMedia.setNumber(number);
            selectedMedia.setAuthor(author);
            selectedMedia.setCreationTimestamp(System.currentTimeMillis());

            Tombola selectedTombola = viewModel.getSelectedTombola().getValue();

            assert selectedTombola != null;

            if (!selectedTombola.containsMediaId(selectedMedia.getId()))
                selectedTombola.addMedia(selectedMedia);

            tombolasActivity.switchToCreateTombola();
        }
    }
}