package com.example.tombolator.tombolas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;

import java.util.Objects;

public class TombolaDetailsFragment extends Fragment {

    TombolasActivity parent;

    public static TombolaDetailsFragment newInstance(TombolasActivity parent) {
        return new TombolaDetailsFragment(parent);
    }

    private TombolaDetailsFragment(TombolasActivity parent) {
        this.parent = parent;
    }

    private TombolasActivityViewModel tombolaViewModel;
    private MediaActivityViewModel mediaActivityViewModel;

    private TextView nameValue;
    private TextView createdAt;

    private LinearLayout availableMedia;

    private Button backButton;
    private Button deleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolaViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_tombola_details, container, false);

        nameValue = layout.findViewById(R.id.name_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        availableMedia = layout.findViewById(R.id.linear_layout_available_media);

        backButton = layout.findViewById(R.id.button_back);
        deleteButton = layout.findViewById(R.id.button_delete);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
        tombolaViewModel.getSelectedTombola().observe(Objects.requireNonNull(this.getActivity()), new SelectedMediaObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
                parent.switchToTombolasMainView();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Tombola tombola = tombolaViewModel.getSelectedTombola().getValue();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                        tombolaDao.deleteMedia(tombola);
                    }
                });

                tombolaViewModel.removeTombola(Objects.requireNonNull(tombola).getId());

                resetForm();
                parent.switchToTombolasMainView();
            }
        });
    }

    private class SelectedMediaObserver implements Observer<Tombola> {

        @Override
        public void onChanged(Tombola tombola) {

            nameValue.setText(tombola.getName());
            createdAt.setText(String.valueOf(tombola.getCreationTimestamp()));

            for(Media media : tombola.getAllMedia()) {

                long id = media.getId();
                String name = media.getName();
                String title = media.getTitle();
                int number = media.getNumber();
                String type = media.getType();

                String mediaString = "[" + id + "] " + type + ": " + name + " - " + title + " (" + number + ")";

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setTypeface(getResources().getFont(R.font.comic_sans_ms));
                textView.setTextSize(20);
                textView.setText(mediaString);
                textView.setId((int) id);

                availableMedia.addView(textView);
            }
        }
    }

    private void resetForm() {
        availableMedia.removeAllViews();
    }
}