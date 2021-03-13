package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MediaActivityModel extends ViewModel {

    private LiveData<List<Media>> mediaDatabase;

    public LiveData<List<Media>> getMediaDatabase() {
        return mediaDatabase;
    }
}
