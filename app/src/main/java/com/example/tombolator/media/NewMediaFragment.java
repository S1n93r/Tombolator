package com.example.tombolator.media;

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

public class NewMediaFragment extends Fragment {

    MediaActivity parent;

    public static NewMediaFragment newInstance(MediaActivity parent) {
        return new NewMediaFragment(parent);
    }

    private NewMediaFragment(MediaActivity parent) {
        this.parent = parent;
    }

    private View layout;
    private MediaActivityModel mediaViewModel;

    private TextView nameText;

    private Button saveButton;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaViewModel = new ViewModelProvider(this).get(MediaActivityModel.class);

        layout = inflater.inflate(R.layout.fragment_new_media, container, false);

        nameText = layout.findViewById(R.id.text_name);

        saveButton = layout.findViewById(R.id.button_save);
        backButton = layout.findViewById(R.id.button_back);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToStartView();
            }
        });
    }
}