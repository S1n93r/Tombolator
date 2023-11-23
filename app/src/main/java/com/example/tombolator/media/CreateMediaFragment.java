package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;

import java.util.Arrays;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class CreateMediaFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private Fragment fragmentBefore;

    private Spinner mediaTypesSpinner;
    private Spinner contentTypeSpinner;
    private TextView contentTypeTextView;

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

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.create_media_fragment, container, false);

        mediaTypesSpinner = layout.findViewById(R.id.spinner_media_types);
        contentTypeSpinner = layout.findViewById(R.id.spinner_content_types);
        contentTypeTextView = layout.findViewById(R.id.text_view_content_type);

        editTextName = layout.findViewById(R.id.edit_text_name);
        editTextTitle = layout.findViewById(R.id.edit_text_title);
        editTextNumber = layout.findViewById(R.id.edit_text_number);
        editTextAuthor = layout.findViewById(R.id.edit_text_author);

        saveButton = layout.findViewById(R.id.button_save);
        backButton = layout.findViewById(R.id.back_button);

        setUpMediaTypeSpinner();
        setUpContentTypeSpinner();
        registerOnClickListener();
        registerObserver();

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

        mediaTypesSpinner.setAdapter(arrayAdapter);

        mediaTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == MediaType.MEAL.ordinal()) {

                    showContentSpinnerAndLabel(false);
                    return;
                }

                showContentSpinnerAndLabel(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /* TODO: When is this triggered? */
            }
        });
    }

    private void showContentSpinnerAndLabel(boolean show) {

        int visibility = View.INVISIBLE;

        if (show)
            visibility = View.VISIBLE;

        contentTypeTextView.setVisibility(visibility);
        contentTypeSpinner.setVisibility(visibility);
    }

    private void setUpContentTypeSpinner() {

        List<String> contentTypeForSpinner = StreamSupport.stream(Arrays.asList(ContentType.values()))
                .map(ContentType::getCleanName)
                .collect(Collectors.toList());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.requireActivity(), R.layout.media_type_spinner_item, contentTypeForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);

        contentTypeSpinner.setAdapter(arrayAdapter);
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> mediaActivity.switchToView(fragmentBefore));

        saveButton.setOnClickListener(new SaveMediaListener());
    }

    private void registerObserver() {
        mediaActivityViewModel.getSelectedMedia().observe(getViewLifecycleOwner(), new SelectedMediaObserver());
    }

    public void setFragmentBefore(Fragment fragmentBefore) {
        this.fragmentBefore = fragmentBefore;
    }

    private class SelectedMediaObserver implements Observer<Media> {

        @Override
        public void onChanged(Media media) {

            int mediaTypeIndex = media.getMediaType() == null
                    ? 0 : media.getMediaType().ordinal() - 1;

            int contentTypeIndex = media.getContentType() == null
                    ? 0 : media.getContentType().ordinal();

            mediaTypesSpinner.setSelection(mediaTypeIndex);
            contentTypeSpinner.setSelection(contentTypeIndex);

            editTextName.setText(media.getName() == null ? "" : media.getName());
            editTextTitle.setText(media.getTitle() == null ? "" : media.getTitle());
            editTextAuthor.setText(media.getAuthor() == null ? "" : media.getAuthor());
            editTextNumber.setText(String.valueOf(media.getNumber()));
        }
    }

    private class SaveMediaListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (editTextName.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_name_empty, Toast.LENGTH_LONG).show();
                return;
            }

            if (editTextTitle.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_title_empty, Toast.LENGTH_LONG).show();
                return;
            }

            if (editTextNumber.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_number_empty, Toast.LENGTH_LONG).show();
                return;
            }

            if (editTextAuthor.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_author_empty, Toast.LENGTH_LONG).show();
                return;
            }

            if (mediaActivityViewModel.getSelectedMedia().getValue() == null)
                throw new NullPointerException();

            String mediaTypeString = mediaTypesSpinner.getSelectedItem() != null ?
                    mediaTypesSpinner.getSelectedItem().toString() : "";
            String contentType = contentTypeSpinner.getSelectedItem() != null ?
                    contentTypeSpinner.getSelectedItem().toString() : "";

            Media selectedMedia = mediaActivityViewModel.getSelectedMedia().getValue();

            selectedMedia.setMediaType(MediaType.fromOldString(mediaTypeString));
            selectedMedia.setContentType(ContentType.fromOldString(contentType));

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

            mediaActivityViewModel.insert(selectedMedia);

            mediaActivity.switchToMediaList();
        }
    }
}