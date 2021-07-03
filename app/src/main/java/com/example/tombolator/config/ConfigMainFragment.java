package com.example.tombolator.config;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.StashScript;
import com.example.tombolator.TomboApplication;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolasActivityViewModel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigMainFragment extends Fragment {

    private static final String FILE_EXTENSION = ".csv";
    private static final String FILE_NAME_EXPORT_MEDIA = "export_media";
    private static final String FILE_NAME_EXPORT_TOMBOLAS = "export_tombolas";

    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    public static ConfigMainFragment newInstance() {
        return new ConfigMainFragment();
    }

    private Button exportDatabaseButton;
    private Button resetDatabaseButton;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.config_main_fragment, container, false);

        exportDatabaseButton = layout.findViewById(R.id.button_export);
        resetDatabaseButton = layout.findViewById(R.id.button_reset_media);
        backButton = layout.findViewById(R.id.button_back);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        exportDatabaseButton.setOnClickListener(view -> {

            exportMedia();
            exportTombolas();
        });

        resetDatabaseButton.setOnClickListener((View view) -> AsyncTask.execute(() -> {

            TomboApplication context = ((TomboApplication) Objects.requireNonNull(getActivity())
                    .getApplicationContext());

            new StashScript(context).run();
        }));

        backButton.setOnClickListener(view -> {

            if(getActivity() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            getActivity().finish();
        });
    }

    private void exportMedia() {

        StringBuilder exportedMedia = new StringBuilder();

        if(mediaActivityViewModel.getAllMediaLiveData().getValue() != null) {
            for(Media media : mediaActivityViewModel.getAllMediaLiveData().getValue()) {
                exportedMedia.append(media.toCsv()).append(System.lineSeparator());
            }
        }

        try {

            if(getContext() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            File file = new File(getContext().getExternalFilesDir(null),
                    FILE_NAME_EXPORT_MEDIA + FILE_EXTENSION);

            OutputStream os = getContext().getContentResolver().openOutputStream(Uri.fromFile(file));

            os.write(exportedMedia.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            Toast.makeText(getContext(), "Saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportTombolas() {

        StringBuilder exportedTombolas = new StringBuilder();

        if(tombolasActivityViewModel.getAllTombolas().getValue() != null) {
            for(Tombola tombola : tombolasActivityViewModel.getAllTombolas().getValue()) {
                exportedTombolas.append(tombola.toCsv()).append(System.lineSeparator());
            }
        }

        try {

            if(getContext() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            File file = new File(getContext().getExternalFilesDir(null),
                    FILE_NAME_EXPORT_TOMBOLAS + FILE_EXTENSION);

            OutputStream os = getContext().getContentResolver().openOutputStream(Uri.fromFile(file));

            os.write(exportedTombolas.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            Toast.makeText(getContext(), "Saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}