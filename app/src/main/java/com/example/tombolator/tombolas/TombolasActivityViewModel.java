package com.example.tombolator.tombolas;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tombolator.TomboRepository;
import com.example.tombolator.media.Media;

import java.util.List;

public class TombolasActivityViewModel extends AndroidViewModel {

    private final TomboRepository tomboRepository;
    private final LiveData<List<Tombola>> allTombolas;

    private final MutableLiveData<Tombola> selectedTombola = new MutableLiveData<>();

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    public TombolasActivityViewModel(@NonNull Application application) {

        super(application);

        tomboRepository = new TomboRepository(application);
        allTombolas = tomboRepository.getAllTombolasLiveData();
    }

    public void insertTombola(Tombola tombola) {
        tomboRepository.insertTombola(tombola);
    }

    public void insertAllTombolas(List<Tombola> tombolaList) {
        tomboRepository.insertAllTombolas(tombolaList);
    }

    public void updateTombola(Tombola tombola) {
        tomboRepository.updateTombola(tombola);
    }

    public void updateAllTombolas(List<Tombola> tombolaList) {
        tomboRepository.updateAllTombolas(tombolaList);
    }

    public void deleteTombola(Tombola tombola) {
        tomboRepository.deleteTombola(tombola);
    }

    public void deleteAllTombolas() {
        tomboRepository.deleteAllTombolas();
    }

    public void deleteMediaFromAllTombolas(Media media) {
        tomboRepository.deleteMediaFromAllTombolas(media);
    }

    public void selectTombola(Tombola tombola) {
        selectedTombola.postValue(tombola);
    }

    public void selectMedia(Media media) {
        selectedMedia.postValue(media);
    }

    public void selectTombola(long tombolaId) {

        if (allTombolas.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        for (Tombola tombola : allTombolas.getValue()) {

            if (tombola.getId() == tombolaId) {

                selectedTombola.setValue(tombola);
                selectedTombola.postValue(selectedTombola.getValue());
                return;
            }
        }

        System.err.println("Tombola with id " + tombolaId + " was not found in " + this.getClass() + ".");
    }

    public LiveData<Tombola> getSelectedTombola() {
        return selectedTombola;
    }

    public LiveData<List<Tombola>> getAllTombolas() {
        return allTombolas;
    }

    public LiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }

    public void insert(Media media) {
        tomboRepository.insertMedia(media);
    }

    public void insertAll(List<Media> mediaList) {
        tomboRepository.insertAllMedia(mediaList);
    }

    public void update(Media media) {
        tomboRepository.updateMedia(media);
    }

    public void updateAll(List<Media> mediaList) {
        tomboRepository.updateAllMedia(mediaList);
    }

    public void delete(Media media) {
        tomboRepository.deleteMedia(media);
    }

    public void deleteAllMedia() {
        tomboRepository.deleteAllMedia();
    }
}