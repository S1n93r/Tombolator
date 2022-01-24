package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

public class MediaCreationStepTwoDescriptionsFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private EditText editTextName;
    private EditText editTextTitle;
    private EditText editTextNumber;
    private EditText editTextAuthor;

    private Button backButton;
    private Button saveButton;

    private MediaCreationStepTwoDescriptionsFragment() {}

    public static MediaCreationStepTwoDescriptionsFragment newInstance() {
        return new MediaCreationStepTwoDescriptionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_creation_step_two_descriptions_fragment, container, false);

        editTextName = layout.findViewById(R.id.edit_text_name);
        editTextTitle = layout.findViewById(R.id.edit_text_title);
        editTextNumber = layout.findViewById(R.id.edit_text_number);
        editTextAuthor = layout.findViewById(R.id.edit_text_author);

        backButton = layout.findViewById(R.id.back_button);
        saveButton = layout.findViewById(R.id.button_contiue);

        registerOnClickListener();
        setVisibilitiesByMediaType();
        registerObserver();

        return layout;
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> mediaActivity.switchToCreationStepOne());

        saveButton.setOnClickListener(new SaveMediaListener());
    }

    private void registerObserver() {
        mediaActivityViewModel.getSelectedMedia().observe(this, new SelectedMediaObserver());
    }

    private void setVisibilitiesByMediaType() {

        if(mediaActivityViewModel.getSelectedMedia().getValue() == null) {
            /* TODO: Add log entry */
            throw new NullPointerException();
        }

        String type = mediaActivityViewModel.getSelectedMedia().getValue().getMediaType();

        switch(type) {
            case Media.MediaType.CASSETTE:
            case Media.MediaType.CD:
            case Media.ContentType.AUDIO_PLAY:
                editTextAuthor.setVisibility(View.GONE);
                break;

            case Media.MediaType.DVD:
            case Media.MediaType.BLU_RAY:
            case Media.ContentType.MOVIE:
                editTextAuthor.setVisibility(View.GONE);
                editTextNumber.setVisibility(View.GONE);
                break;

            case Media.MediaType.BOOK:
            case Media.MediaType.E_BOOK:
                editTextNumber.setVisibility(View.GONE);
                break;
        }
    }

    private class SelectedMediaObserver implements Observer<Media> {

        @Override
        public void onChanged(Media media) {

            editTextName.setText(media.getName());
            editTextTitle.setText(media.getTitle());
            editTextNumber.setText(String.valueOf(media.getNumber()));
            editTextAuthor.setText(media.getAuthor());
        }
    }

    private class SaveMediaListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(editTextName.getText().length() > 0){

                String name = editTextName.getText() != null ? editTextName.getText().toString() : "";
                String title = editTextTitle.getText() != null ? editTextTitle.getText().toString() : "";
                String numberAsString = editTextNumber.getText() != null ? editTextNumber.getText().toString() : "";
                String author = editTextAuthor.getText() != null ? editTextAuthor.getText().toString() : "";

                int number = numberAsString.length() > 0 ? Integer.parseInt(numberAsString) : -1;

                if(mediaActivityViewModel.getSelectedMedia().getValue() == null) {
                    /* TODO: Write NPE to log */
                    throw new NullPointerException();
                }

                Media selectedMedia = mediaActivityViewModel.getSelectedMedia().getValue();

                selectedMedia.setName(name);
                selectedMedia.setTitle(title);
                selectedMedia.setNumber(number);
                selectedMedia.setAuthor(author);
                selectedMedia.setCreationTimestamp(System.currentTimeMillis());

                mediaActivityViewModel.insert(selectedMedia);

                mediaActivity.switchToMediaListStepTwo();
            }
        }
    }
}