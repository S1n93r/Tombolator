package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MediaActivityModel extends ViewModel {

    private List<Media> mediaDatabase = new ArrayList<>();
    private MutableLiveData<List<Media>> mediaDatabaseLiveData = new MutableLiveData<>();

    public MediaActivityModel() {

        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public void addMedia(Media media) {

        mediaDatabase.add(media);
        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public LiveData<List<Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }
}
