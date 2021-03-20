package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MediaActivityViewModel extends ViewModel {

    final private List<Media> mediaDatabase = new ArrayList<>();
    final private MutableLiveData<List<Media>> mediaDatabaseLiveData = new MutableLiveData<>();

    public MediaActivityViewModel() {
        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public void addMedia(List<Media> mediaList) {

        mediaDatabase.clear();
        mediaDatabase.addAll(mediaList);

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