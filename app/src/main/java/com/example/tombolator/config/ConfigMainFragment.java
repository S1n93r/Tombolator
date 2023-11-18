package com.example.tombolator.config;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tombolator.R;
import com.example.tombolator.TomboApplication;
import com.example.tombolator.media.ContentType;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;
import com.example.tombolator.media.MediaType;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolaDao;
import com.example.tombolator.utils.DateUtil;
import com.example.tombolator.utils.ToasterUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigMainFragment extends Fragment {

    private static final String FILE_EXTENSION = ".csv";
    private static final String FILE_NAME_EXPORT_MEDIA = "export_media";
    private static final String FILE_NAME_EXPORT_TOMBOLAS = "export_tombolas";
    private TextView exportVersionTextView;
    private Button exportDatabaseButton;
    private Button importDatabaseButton;
    private ImageView backButton;

    public static ConfigMainFragment newInstance() {
        return new ConfigMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.config_main_fragment, container, false);

        exportVersionTextView = layout.findViewById(R.id.text_view_export_version);

        exportDatabaseButton = layout.findViewById(R.id.button_export);
        importDatabaseButton = layout.findViewById(R.id.button_import);
        backButton = layout.findViewById(R.id.back_button);

        registerOnClickListener();

        refreshExportedVersion();

        return layout;
    }

    private void registerOnClickListener() {

        exportDatabaseButton.setOnClickListener(view -> {

            TomboApplication context = ((TomboApplication) requireActivity()
                    .getApplicationContext());

            exportMedia(context);
            exportTombolas(context);

            /* FIXME: This call does not update. Check why. */
            refreshExportedVersion();
        });

        importDatabaseButton.setOnClickListener((View view) -> AsyncTask.execute(() -> {

            TomboApplication context = ((TomboApplication) requireActivity()
                    .getApplicationContext());

            importMedia(context);
            importTombolas(context);
        }));

        backButton.setOnClickListener(view -> requireActivity().finish());
    }

    private void exportMedia(TomboApplication context) {

        AsyncTask.execute(() -> {

            MediaDao mediaDao = context.getTomboDb().mediaDao();

            StringBuilder exportedMedia = new StringBuilder();

            for (Media media : mediaDao.getAllMedia()) {
                exportedMedia.append(media.toCsv()).append(System.lineSeparator());
            }

            try {

                File file = new File(requireContext().getExternalFilesDir(null),
                        FILE_NAME_EXPORT_MEDIA + FILE_EXTENSION);

                OutputStream os = getContext().getContentResolver().openOutputStream(Uri.fromFile(file));

                os.write(exportedMedia.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                ToasterUtil.makeShortToast(requireActivity(), getContext(),
                        "Save to {0}", file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void exportTombolas(TomboApplication context) {

        AsyncTask.execute(() -> {

            TombolaDao tombolaDao = context.getTomboDb().tombolaDao();

            StringBuilder exportedTombolas = new StringBuilder();

            for (Tombola tombola : tombolaDao.getAllTombolas()) {
                exportedTombolas.append(tombola.toCsv()).append(System.lineSeparator());
            }

            try {

                File file = new File(requireContext().getExternalFilesDir(null),
                        FILE_NAME_EXPORT_TOMBOLAS + FILE_EXTENSION);

                OutputStream os = getContext().getContentResolver().openOutputStream(Uri.fromFile(file));

                os.write(exportedTombolas.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(),
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

                File file = new File(requireContext().getExternalFilesDir(null),
                        FILE_NAME_EXPORT_MEDIA + FILE_EXTENSION);

                BufferedReader csvReader = new BufferedReader(new FileReader(file.getAbsolutePath()));

                String row;

                while ((row = csvReader.readLine()) != null) {

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
                    media.setMediaType(MediaType.fromOldString(mediaType));
                    media.setContentType(ContentType.fromOldString(contentType));

                    mediaList.add(media);
                }

                MediaDao mediaDao = context.getTomboDb().mediaDao();
                mediaDao.insertAllMedia(mediaList);

                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Medien aus " + file.getAbsolutePath() + " importiert.", Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void importTombolas(TomboApplication context) {

        AsyncTask.execute(() -> {

            try {

                List<Tombola> tombolaList = new ArrayList<>();

                File file = new File(requireContext().getExternalFilesDir(null),
                        FILE_NAME_EXPORT_TOMBOLAS + FILE_EXTENSION);

                BufferedReader csvReader = new BufferedReader(new FileReader(file.getAbsolutePath()));

                MediaDao mediaDao = context.getTomboDb().mediaDao();

                String row;

                while ((row = csvReader.readLine()) != null) {

                    String[] data = row.split(";");

                    String id = data[0];
                    String timestamp = data[1];
                    String name = data[2];
                    String type = data[3];
                    String mediaAvailable = data[4];
                    String mediaDrawn = null;

                    /* TODO: Seems a bit hacky. */
                    try {
                        mediaDrawn = data[5];
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //
                    }

                    Pattern pattern = Pattern.compile("\\(\\d+\\)");

                    List<Media> mediaAvailableList = new ArrayList<>();

                    if (mediaAvailable != null) {

                        Matcher mediaIdsAvailable = pattern.matcher(mediaAvailable);

                        while (mediaIdsAvailable.find()) {

                            int start = mediaIdsAvailable.start() + 1;
                            int end = mediaIdsAvailable.end() - 1;

                            String mediaId = mediaAvailable.substring(start, end);

                            Media media = mediaDao.getMedia(Long.parseLong(mediaId));
                            mediaAvailableList.add(media);
                        }
                    }

                    List<Media> mediaDrawnList = new ArrayList<>();

                    if (mediaDrawn != null) {

                        Matcher mediaIdsDrawn = pattern.matcher(mediaDrawn);

                        while (mediaIdsDrawn.find()) {

                            int start = mediaIdsDrawn.start() + 1;
                            int end = mediaIdsDrawn.end() - 1;

                            String mediaId = mediaDrawn.substring(start, end);

                            Media media = mediaDao.getMedia(Long.parseLong(mediaId));
                            mediaDrawnList.add(media);
                        }
                    }

                    Tombola tombola = new Tombola();
                    tombola.setId(Long.parseLong(id));
                    tombola.setCreationTimestamp(Long.parseLong(timestamp));
                    tombola.setName(name);
                    tombola.setType(Tombola.Type.valueOf(type));

                    tombola.setMediaAvailable(mediaAvailableList);
                    tombola.setMediaDrawn(mediaDrawnList);

                    tombolaList.add(tombola);
                }

                TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                tombolaDao.insertAllTombolas(tombolaList);

                requireActivity().runOnUiThread(() -> Toast.makeText(getContext(),
                        "Tombolas aus " + file.getAbsolutePath() + " importiert.", Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void refreshExportedVersion() {

        File file = new File(requireContext().getExternalFilesDir(null),
                FILE_NAME_EXPORT_MEDIA + FILE_EXTENSION);

        long lastModified = file.lastModified();

        String dateFormatted = lastModified == 0 ? "[Kein Export gefunden]" : DateUtil.formatDateAndTime(lastModified);

        exportVersionTextView.setText(dateFormatted);
    }
}