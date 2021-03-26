package com.example.tombolator.tombolas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;

public class TombolasActivityViewModel extends ViewModel {

    private final MutableLiveData<Tombola> selectedTombola = new MutableLiveData<>();

    private final MutableLiveData<HashMap<Long, Tombola>> tombolaDatabase = new MutableLiveData<>(new HashMap<Long, Tombola>());

    public void addTombola(List<Tombola> tombolaList) {

        if(tombolaDatabase.getValue() == null)
            throw new NullPointerException();

        tombolaDatabase.getValue().clear();

        for(Tombola tombola : tombolaList)
            tombolaDatabase.getValue().put(tombola.getId(), tombola);

        tombolaDatabase.postValue(tombolaDatabase.getValue());
    }

    public void removeAllTombolas() {

        if(tombolaDatabase.getValue() == null)
            throw new NullPointerException();

        tombolaDatabase.getValue().clear();

        tombolaDatabase.postValue(tombolaDatabase.getValue());
    }

    public void removeTombola(long tombolaId) {

        if(tombolaDatabase.getValue() == null)
            throw new NullPointerException();

        tombolaDatabase.getValue().remove(tombolaId);
    }

    public void selectTombola(long tombolaId) {

        if(tombolaDatabase.getValue() == null)
            throw new NullPointerException();

        selectedTombola.postValue(tombolaDatabase.getValue().get(tombolaId));
    }

    public MutableLiveData<Tombola> getSelectedTombola() {
        return selectedTombola;
    }

    public LiveData<HashMap<Long, Tombola>> getTombolaDatabase() {
        return tombolaDatabase;
    }
}