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
import com.example.tombolator.TomboApplication;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.media.MediaDao;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolaDao;
import com.example.tombolator.tombolas.TombolasActivityViewModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
    private Button importDatabaseButton;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.config_main_fragment, container, false);

        exportDatabaseButton = layout.findViewById(R.id.button_export);
        importDatabaseButton = layout.findViewById(R.id.button_import);
        backButton = layout.findViewById(R.id.button_back);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        exportDatabaseButton.setOnClickListener(view -> {

                TomboApplication context = ((TomboApplication) Objects.requireNonNull(getActivity())
                        .getApplicationContext());

                exportMedia(context);
                exportTombolas(context);
        });

        importDatabaseButton.setOnClickListener((View view) -> AsyncTask.execute(() -> {

            TomboApplication context = ((TomboApplication) Objects.requireNonNull(getActivity())
                    .getApplicationContext());

            importMedia(context);
            importTombolas(context);
        }));

        backButton.setOnClickListener(view -> {

            if(getActivity() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            getActivity().finish();
        });
    }

    private void exportMedia(TomboApplication context) {

        AsyncTask.execute(() -> {

            MediaDao mediaDao = context.getTomboDb().mediaDao();

            StringBuilder exportedMedia = new StringBuilder();

            for(Media media : mediaDao.getAllMedia()) {
                exportedMedia.append(media.toCsv()).append(System.lineSeparator());
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

                getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void exportTombolas(TomboApplication context) {

        AsyncTask.execute(() -> {

            TombolaDao tombolaDao = context.getTomboDb().tombolaDao();

            StringBuilder exportedTombolas = new StringBuilder();

            for(Tombola tombola : tombolaDao.getAllTombolas()) {
                exportedTombolas.append(tombola.toCsv()).append(System.lineSeparator());
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

                getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void importMedia(TomboApplication context) {

        AsyncTask.execute(() -> {

            try {

                List<Media> mediaList = new ArrayList<>();

                File file = new File(getContext().getExternalFilesDir(null),
                        FILE_NAME_EXPORT_MEDIA + FILE_EXTENSION);

                BufferedReader csvReader = new BufferedReader(new FileReader(file.getAbsolutePath()));

                String row;

                while((row = csvReader.readLine()) != null) {

                    String[] data = row.split(";");

                    String id = data[0];
                    String timestamp = data[1];
                    String name = data[2];
                    String title = data[3];
                    String number = data[4];
                    //????
                    String mediaType = data[6];
                    String contentType = data[7];

                    Media media = new Media();
                    media.setId(Long.parseLong(id));
                    media.setCreationTimestamp(Long.parseLong(timestamp));
                    media.setName(name);
                    media.setTitle(title);
                    media.setNumber(Integer.parseInt(number));
                    media.setMediaType(mediaType);
                    media.setContentType(contentType);

                    mediaList.add(media);
                }

                MediaDao mediaDao = context.getTomboDb().mediaDao();
                mediaDao.insertAllMedia(mediaList);

                getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Medien aus [File] importiert.", Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void importTombolas(TomboApplication context) {

        AsyncTask.execute(() -> {

            try {

                List<Tombola> tombolaList = new ArrayList<>();

                File file = new File(getContext().getExternalFilesDir(null),
                        FILE_NAME_EXPORT_TOMBOLAS + FILE_EXTENSION);

                BufferedReader csvReader = new BufferedReader(new FileReader(file.getAbsolutePath()));

                String row;

                while((row = csvReader.readLine()) != null) {

                    String[] data = row.split(";");

                    String id = data[0];
                    String timestamp = data[1];
                    String name = data[2];
                    String type = data[3];
                    String mediaList = data[4];

                    /* TODO: Write code to extract media from mediaList string. */

                    Tombola tombola = new Tombola();
                    tombola.setId(Long.parseLong(id));
                    tombola.setCreationTimestamp(Long.parseLong(timestamp));
                    tombola.setName(name);
                    tombola.setType(Tombola.Type.valueOf(type));

                    tombolaList.add(tombola);
                }

                TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                tombolaDao.insertAllTombolas(tombolaList);

                getActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Tombolas aus " + file.getAbsolutePath() + " importiert.", Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}