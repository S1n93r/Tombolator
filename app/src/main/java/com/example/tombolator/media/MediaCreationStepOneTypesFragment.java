package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

public class MediaCreationStepOneTypesFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private Fragment fragmentBefore;

    private Spinner mediaTypesSpinner;
    private Spinner contentTypeSpinner;

    private TextView contentTypeTextView;

    private Button continueButton;
    private Button backButton;

    private MediaCreationStepOneTypesFragment() {
    }

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

        mediaTypesSpinner = layout.findViewById(R.id.spinner_media_types);
        contentTypeSpinner = layout.findViewById(R.id.spinner_content_types);

        contentTypeTextView = layout.findViewById(R.id.text_view_content_type);

        continueButton = layout.findViewById(R.id.button_continue);
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
                this.getActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

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
                this.getActivity(), R.layout.media_type_spinner_item, contentTypeForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);

        contentTypeSpinner.setAdapter(arrayAdapter);
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> mediaActivity.switchToView(fragmentBefore));

        continueButton.setOnClickListener(v -> {

            String mediaTypeString = mediaTypesSpinner.getSelectedItem() != null ?
                    mediaTypesSpinner.getSelectedItem().toString() : "";
            String contentType = contentTypeSpinner.getSelectedItem() != null ?
                    contentTypeSpinner.getSelectedItem().toString() : "";

            if (mediaActivityViewModel.getSelectedMedia().getValue() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            Media selectedMedia = mediaActivityViewModel.getSelectedMedia().getValue();

            selectedMedia.setMediaType(MediaType.fromOldString(mediaTypeString));
            selectedMedia.setContentType(ContentType.fromOldString(contentType));

            mediaActivity.switchToCreationStepTwo();
        });
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
                    ? 0 : media.getMediaType().ordinal();

            int contentTypeIndex = media.getContentType() == null
                    ? 0 : media.getContentType().ordinal();

            mediaTypesSpinner.setSelection(mediaTypeIndex);
            contentTypeSpinner.setSelection(contentTypeIndex);
        }
    }
}