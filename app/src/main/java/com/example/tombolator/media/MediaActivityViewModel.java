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

    public void addMedia(List<Media> mediaList) {

        for(Media media : mediaList)
            mediaDatabase.add(media);

        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public void removeAllMedia() {
        mediaDatabase.clear();
        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public LiveData<List<Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }
}