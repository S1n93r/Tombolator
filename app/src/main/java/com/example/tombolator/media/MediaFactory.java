package com.example.tombolator.media;

import java.util.HashMap;
import java.util.Map;

public class MediaFactory {

    private static MediaFactory INSTANCE = null;

    public static MediaFactory getInstance() {

        if(INSTANCE == null)
            INSTANCE = new MediaFactory();

        return INSTANCE;
    }

    private MediaFactory() {}

    Map<Integer, Media> mediaDatabase = new HashMap<>();

    public Media getOrCreateMedia(String name) {

        int id = mediaDatabase.size() + 1;

        Media media = new Media(id, name);
        mediaDatabase.put(id, media);

        return media;
    }
}