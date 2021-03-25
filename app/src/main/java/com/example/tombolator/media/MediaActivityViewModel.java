package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaActivityViewModel extends ViewModel {

    final private MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    final private Map<Long, Media> mediaDatabase = new HashMap<>();
    final private MutableLiveData<Map<Long, Media>> mediaDatabaseLiveData = new MutableLiveData<>();

    public MediaActivityViewModel() {
        mediaDatabaseLiveData.setValue(mediaDatabase);
    }

    public void addMedia(List<Media> mediaList) {

        mediaDatabase.clear();

        for(Media media : mediaList)
            mediaDatabase.put(media.getId(), media);

        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public void removeMedia(long mediaId) {

        mediaDatabase.remove(mediaId);

        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public void removeAllMedia() {

        mediaDatabase.clear();
        
        mediaDatabaseLiveData.postValue(mediaDatabase);
    }

    public void selectMedia(long mediaId) {

        Media media = mediaDatabase.get(mediaId);

        if(media == null) {
            System.err.println("Medium not found. Selected media remains null.");
            return;
        }

        selectedMedia.postValue(media);
    }

    public LiveData<Map<Long, Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }
}