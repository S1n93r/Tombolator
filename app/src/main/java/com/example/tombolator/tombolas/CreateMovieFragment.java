package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.media.Media;

public class CreateMovieFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel viewModel;

    private EditText editTextTitle;

    private ImageView saveButton;
    private ImageView backButton;

    private CreateMovieFragment() {
    }

    public static CreateMovieFragment newInstance() {
        return new CreateMovieFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        viewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.create_movie_fragment, container, false);

        editTextTitle = layout.findViewById(R.id.edit_title);

        saveButton = layout.findViewById(R.id.save_button);
        backButton = layout.findViewById(R.id.back_button);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View v) -> tombolasActivity.switchToChooseMedia());

        saveButton.setOnClickListener(new SaveMediaListener());
    }

    private class SaveMediaListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (editTextTitle.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_title_empty, Toast.LENGTH_LONG).show();
                return;
            }

            Media selectedMedia = viewModel.getSelectedMedia().getValue();

            assert selectedMedia != null;

            String title = editTextTitle.getText() != null ? editTextTitle.getText().toString() : "";

            selectedMedia.setName(title);
            selectedMedia.setCreationTimestamp(System.currentTimeMillis());

            Tombola selectedTombola = viewModel.getSelectedTombola().getValue();

            assert selectedTombola != null;

            if (!selectedTombola.containsMediaId(selectedMedia.getId()))
                selectedTombola.addMedia(selectedMedia);

            tombolasActivity.switchToCreateTombola();
        }
    }
}