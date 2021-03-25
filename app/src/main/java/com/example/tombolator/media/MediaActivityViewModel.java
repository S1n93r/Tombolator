package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MediaActivityViewModel extends ViewModel {

    final private MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

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

    public void removeMedia(Media media) {

        mediaDatabase.clear();
        mediaDatabase.remove(media);

        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public void removeAllMedia() {

        mediaDatabase.clear();
        
        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public void selectMedia(long mediaId) {

        boolean success = false;

        for(Media media : mediaDatabase) {
            if(media.getId() == mediaId) {

                selectedMedia.postValue(media);
                success = true;
            }
        }

        if(success == false)
            System.err.println("Medium not found. Selected media remains null.");
    }

    public LiveData<List<Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }
}