package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MediaActivityViewModel extends ViewModel {

    private final MediaFactory mediaFactory = MediaFactory.getInstance();

    private List<Media> mediaDatabase = new ArrayList<>();
    private MutableLiveData<List<Media>> mediaDatabaseLiveData = new MutableLiveData<>();

    public MediaActivityViewModel() {

        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public void addMedia(Media media) {

        mediaDatabase.add(media);
        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public void addMedia(String name, String title, int number, String type) {

        Media media = mediaFactory.getOrCreateMedia(name, title, number, type);

        mediaDatabase.add(media);
        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public LiveData<List<Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }
}