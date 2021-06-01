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

import java.util.List;

public class MediaCreationStepOneTypesFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private Spinner mediaTypeSpinner;
    private Spinner contentTypeSpinner;

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
        contentTypeSpinner = layout.findViewById(R.id.spinner_content_types);

        continueButton = layout.findViewById(R.id.button_continue);
        backButton = layout.findViewById(R.id.button_back);

        setUpMediaTypeSpinner();
        setUpContentTypeSpinner();
        registerOnClickListener();

        return layout;
    }

    @Override
    public void onStart() {

        super.onStart();

        updateView();
    }

    private void setUpMediaTypeSpinner() {

        List<String> mediaTypesForSpinner = Media.MediaType.getMediaTypes();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);

        mediaTypeSpinner.setAdapter(arrayAdapter);
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

            String mediaType = mediaTypeSpinner.getSelectedItem() != null ? mediaTypeSpinner.getSelectedItem().toString() : "";
            String contentType = contentTypeSpinner.getSelectedItem() != null ? contentTypeSpinner.getSelectedItem().toString() : "";

            if(mediaActivityViewModel.getSelectedMedia() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            mediaActivityViewModel.getSelectedMedia().setMediaType(mediaType);
            mediaActivityViewModel.getSelectedMedia().setContentType(contentType);

            mediaActivity.switchToCreationStepTwo();
        });
    }

    private void updateView() {

        Media media = mediaActivityViewModel.getSelectedMedia();

        int mediaTypeIndex = media.getMediaType() == null
                ? 0 : Media.MediaType.getIndex(media.getMediaType());

        int contentTypeIndex = media.getContentType() == null
                ? 0 : Media.ContentType.getIndex(media.getContentType());

        mediaTypeSpinner.setSelection(mediaTypeIndex);
        contentTypeSpinner.setSelection(contentTypeIndex);
    }
}