package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class MediaActivityViewModel extends ViewModel {

    final private MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    final private MutableLiveData<HashMap<Long, Media>> mediaDatabaseLiveData = new MutableLiveData<>(new HashMap<Long, Media>());

    public void addMedia(List<Media> mediaList) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseLiveData.getValue().clear();

        for(Media media : mediaList)
            mediaDatabaseLiveData.getValue().put(media.getId(), media);

        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
    }

    public void removeMedia(long mediaId) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseLiveData.getValue().remove(mediaId);

        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
    }

    public void removeAllMedia() {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseLiveData.getValue().clear();
        
        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
    }

    public void selectMedia(long mediaId) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        Media media = mediaDatabaseLiveData.getValue().get(mediaId);

        if(media == null) {
            System.err.println("Medium not found. Selected media remains null.");
            return;
        }

        selectedMedia.postValue(media);
    }

    public LiveData<HashMap<Long, Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }
}