package com.example.tombolator.tombolas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TombolasActivityViewModel extends ViewModel {

    private final List<Tombola> tombolaDatabase = new ArrayList<>();
    private final MutableLiveData<List<Tombola>> tombolaDatabaseLiveData = new MutableLiveData<>();

    public TombolasActivityViewModel() {
        tombolaDatabaseLiveData.setValue(tombolaDatabase);
    }

    public void addTombola(List<Tombola> tombolaList) {

        tombolaDatabase.clear();
        tombolaDatabase.addAll(tombolaList);

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