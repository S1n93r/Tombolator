package com.example.tombolator;

import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;

import java.util.ArrayList;
import java.util.List;

public class SusisStashScript implements Runnable {

    private final TomboDbApplication context;

    public SusisStashScript(TomboDbApplication context) {
        this.context = context;
    }

    @Override
    public void run() {

        for(Media media : createMediaList()) {
            MediaDao mediaDao = context.getTomboDb().mediaDao();
            mediaDao.insertMedia(media);
        }
    }

    private List<Media> createMediaList() {

        List<Media> mediaList = new ArrayList<>();

        /* TODO: Load from csv here. */
        Media media = new Media("", "", 0 ,"");

        return mediaList;
    }
}