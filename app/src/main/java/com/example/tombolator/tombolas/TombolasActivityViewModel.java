package com.example.tombolator.tombolas;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.tombolator.TomboRepository;

import java.util.List;

public class TombolasActivityViewModel extends AndroidViewModel {

    private final TomboRepository tomboRepository;
    private final LiveData<List<Tombola>> allTombolas;

    private final MutableLiveData<Tombola> selectedTombola = new MutableLiveData<>();

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

    public void deletAllTombolas() {
        tomboRepository.deleteAllTombolas();
    }

    public void selectTombola(Tombola tombola) {
        selectedTombola.setValue(tombola);
        selectedTombola.postValue(selectedTombola.getValue());
    }

    public void selectTombola(long tombolaId) {

        for(Tombola tombola : allTombolas.getValue()) {

            if(tombola.getId() == tombolaId) {

                selectedTombola.setValue(tombola);
                selectedTombola.postValue(selectedTombola.getValue());
                return;
            }
        }

        System.err.println("Tombola with id " + tombolaId + " was not found in " + this.getClass() + ".");
    }

    public MutableLiveData<Tombola> getSelectedTombola() {
        return selectedTombola;
    }

    public LiveData<List<Tombola>> getAllTombolas() {
        return allTombolas;
    }
}