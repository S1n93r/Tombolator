package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.List;

public class MediaCreationStepOneTypesFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private Spinner mediaTypesSpinner;
    private Spinner contentTypeSpinner;

    private TextView contentTypeTextView;

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

        List<String> mediaTypesForSpinner = Media.MediaType.getMediaTypes();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);

        mediaTypesSpinner.setAdapter(arrayAdapter);

        mediaTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == Media.MediaType.getIndex(Media.MediaType.MEAL)) {

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

        if(show)
            visibility = View.VISIBLE;

        contentTypeTextView.setVisibility(visibility);
        contentTypeSpinner.setVisibility(visibility);
    }

    private void setUpContentTypeSpinner() {

        List<String> contentTypeForSpinner = Media.ContentType.getContentTypes();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, contentTypeForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);

        contentTypeSpinner.setAdapter(arrayAdapter);
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> mediaActivity.switchToMediaListStepTwo());

        continueButton.setOnClickListener(v -> {

            String mediaType = mediaTypesSpinner.getSelectedItem() != null ? mediaTypesSpinner.getSelectedItem().toString() : "";
            String contentType = contentTypeSpinner.getSelectedItem() != null ? contentTypeSpinner.getSelectedItem().toString() : "";

            if(mediaActivityViewModel.getSelectedMedia().getValue() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            Media selectedMedia = mediaActivityViewModel.getSelectedMedia().getValue();

            selectedMedia.setMediaType(mediaType);
            selectedMedia.setContentType(contentType);

            mediaActivity.switchToCreationStepTwo();
        });
    }

    private void registerObserver() {
        mediaActivityViewModel.getSelectedMedia().observe(this, new SelectedMediaObserver());
    }

    private class SelectedMediaObserver implements Observer<Media> {

        @Override
        public void onChanged(Media media) {

            int mediaTypeIndex = media.getMediaType() == null
                    ? 0 : Media.MediaType.getIndex(media.getMediaType());

            int contentTypeIndex = media.getContentType() == null
                    ? 0 : Media.ContentType.getIndex(media.getContentType());

            mediaTypesSpinner.setSelection(mediaTypeIndex);
            contentTypeSpinner.setSelection(contentTypeIndex);
        }
    }
}