package com.example.tombolator.media;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

import java.util.Objects;

public class MediaCreationStepTwoDescriptionsFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private TextView editTextName;
    private TextView editTextTitle;
    private TextView editTextNumber;
    private TextView editTextAuthor;

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

        backButton = layout.findViewById(R.id.button_back);
        saveButton = layout.findViewById(R.id.button_save);

        registerOnClickListener();
        setVisibilitiesByMediaType();

        return layout;
    }

    private void setVisibilitiesByMediaType() {

        String type = mediaActivityViewModel.getSelectedMedia().getValue().getType();

        switch(type) {
            case Media.Type.CASSETTE:
            case Media.Type.CD:
            case Media.Type.AUDIO_PLAY:
                editTextAuthor.setVisibility(View.GONE);
                break;

            case Media.Type.DVD:
            case Media.Type.BLU_RAY:
            case Media.Type.MOVIE:
                editTextAuthor.setVisibility(View.GONE);
                editTextNumber.setVisibility(View.GONE);
                break;

            case Media.Type.BOOK:
            case Media.Type.E_BOOK:
            case Media.Type.AUDIO_BOOK:
                editTextNumber.setVisibility(View.GONE);
                break;
        }

        if(type == Media.Type.CASSETTE) {
            editTextAuthor.setVisibility(View.GONE);
        }
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(v -> {
            resetForm();
            mediaActivity.switchToCreationStepOne();
        });

        saveButton.setOnClickListener(v -> {

            if(editTextName.getText().length() > 0){

                String name = editTextName.getText() != null ? editTextName.getText().toString() : "";
                String title = editTextTitle.getText() != null ? editTextTitle.getText().toString() : "";
                String numberAsString = editTextNumber.getText() != null ? editTextNumber.getText().toString() : "";
                String author = editTextAuthor.getText() != null ? editTextAuthor.getText().toString() : "";

                int number = numberAsString.length() > 0 ? Integer.parseInt(numberAsString) : -1;

                final Media media = mediaActivityViewModel.getSelectedMedia().getValue();
                media.setName(name);
                media.setTitle(title);
                media.setNumber(number);
                media.setAuthor(author);

                AsyncTask.execute(() -> {

                    TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                            .getApplicationContext());

                    final MediaDao mediaDao = context.getTomboDb().mediaDao();
                    mediaDao.insertMedia(media);
                });

                resetForm();
                mediaActivity.switchToMainView();
            }
        });
    }

    private void resetForm() {

        editTextName.setText("");
        editTextTitle.setText("");
        editTextNumber.setText("");
        editTextAuthor.setText("");
    }
}