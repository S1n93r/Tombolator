package com.example.tombolator;

import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SusisStashScript implements Runnable {

    private final TomboDbApplication context;

    public SusisStashScript(TomboDbApplication context) {
        this.context = context;
    }

    @Override
    public void run() {

        try {

            for(Media media : createMediaList()) {
                MediaDao mediaDao = context.getTomboDb().mediaDao();
                mediaDao.insertMedia(media);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Media> createMediaList() throws IOException {

        List<Media> mediaList = new ArrayList<>();

        InputStream inputStream = new FileInputStream("C:\\Users\\Mirco\\Documents\\GitHub\\Tombolator\\app\\src\\main\\res\\kiddinx\\susis_stash.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        /* Skip headline */
        reader.readLine();

        for (String line; (line = reader.readLine()) != null; ) {

            String[] mediaValues = line.split(";");

            String name = mediaValues[0];
            String title = mediaValues[1];
            int number = Integer.parseInt(mediaValues[2]);
            String type = mediaValues[3];

            Media media = new Media(name, title, number ,type);
            mediaList.add(media);
        }

        return mediaList;
    }
}