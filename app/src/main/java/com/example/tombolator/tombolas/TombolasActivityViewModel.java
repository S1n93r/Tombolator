package com.example.tombolator.tombolas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TombolasActivityViewModel extends ViewModel {

    private final MutableLiveData<Tombola> selectedTombola = new MutableLiveData<>();
    private final Map<Long, Tombola> tombolaDatabase = new HashMap<>();
    private final MutableLiveData<Map<Long, Tombola>> tombolaDatabaseLiveData = new MutableLiveData<>();

    public TombolasActivityViewModel() {
        tombolaDatabaseLiveData.setValue(tombolaDatabase);
    }

    public void addTombola(List<Tombola> tombolaList) {

        tombolaDatabase.clear();

        for(Tombola tombola : tombolaList)
            tombolaDatabase.put(tombola.getId(), tombola);

        tombolaDatabaseLiveData.postValue(tombolaDatabase);
    }

    public void removeAllTombolas() {

        tombolaDatabase.clear();

        tombolaDatabaseLiveData.postValue(tombolaDatabase);
    }

    public void removeTombola(long tombolaId) {
        tombolaDatabase.remove(tombolaId);
    }

    public void selectTombola(long tombolaId) {
        selectedTombola.postValue(tombolaDatabase.get(tombolaId));
    }

    public LiveData<Map<Long, Tombola>> getTombolaDatabase() {
        return tombolaDatabaseLiveData;
    }

    public MutableLiveData<Tombola> getSelectedTombola() {
        return selectedTombola;
    }
}