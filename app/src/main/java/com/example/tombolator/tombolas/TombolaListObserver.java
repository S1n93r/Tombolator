package com.example.tombolator.tombolas;

import android.content.Context;
import android.widget.Spinner;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Objects;

public class TombolaListObserver implements Observer<List<Tombola>> {

    private final Spinner spinner;
    private final Context context;

    public TombolaListObserver(Spinner spinner, Context context) {
        this.spinner = spinner;
        this.context = context;
    }

    @Override
    public void onChanged(List<Tombola> tombolaList) {

        TombolaSpinnerItemAdapter tombolaArrayAdapter = new TombolaSpinnerItemAdapter(
                Objects.requireNonNull(context), tombolaList);

        spinner.setAdapter(tombolaArrayAdapter);
    }
}
