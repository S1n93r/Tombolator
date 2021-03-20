package com.example.tombolator.tombolas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TombolasActivityViewModel extends ViewModel {

    private List<Tombola> tombolaDatabase = new ArrayList<>();
    private MutableLiveData<List<Tombola>> tombolaDatabaseLiveData = new MutableLiveData<>();

    public TombolasActivityViewModel() {
        tombolaDatabaseLiveData.setValue(tombolaDatabase);
    }

    public void addTombola(List<Tombola> tombolaList) {

        tombolaDatabase.clear();

        for(Tombola tombola : tombolaList)
            tombolaDatabase.add(tombola);

        tombolaDatabaseLiveData.postValue(tombolaDatabase);
    }

    public void removeAllTombolas() {
        tombolaDatabase.clear();
        tombolaDatabaseLiveData.postValue(tombolaDatabase);
    }

    public LiveData<List<Tombola>> getTombolaDatabase() {
        return tombolaDatabaseLiveData;
    }
}