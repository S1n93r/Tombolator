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
import com.example.tombolator.R;

public class MediaDetailsFragment extends Fragment {

    MediaActivity parent;

    public static MediaDetailsFragment newInstance(MediaActivity parent) {
        return new MediaDetailsFragment(parent);
    }

    private MediaDetailsFragment(MediaActivity parent) {
        this.parent = parent;
    }

    private TextView nameValue;
    private TextView numberValue;
    private TextView titleValue;
    private TextView typeValue;
    private TextView createdAt;

    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_media_details, container, false);

        nameValue = layout.findViewById(R.id.name_value);
        numberValue = layout.findViewById(R.id.number_value);
        titleValue = layout.findViewById(R.id.title_value);
        typeValue = layout.findViewById(R.id.type_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        backButton = layout.findViewById(R.id.button_back);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToMainView();
            }
        });
    }

    private void resetForm() {

        nameValue.setText("");
        numberValue.setText("");
        titleValue.setText("");
        typeValue.setText("");
        createdAt.setText("");
    }
}