package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.List;

public class TombolaMainFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel tombolasActivityViewModel;
    private LinearLayout availableTombolas;
    private Button backButton;
    private Button newTombolaButton;

    private TombolaMainFragment() {

    }

    public static TombolaMainFragment newInstance() {
        return new TombolaMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombola_list_fragment, container, false);

        availableTombolas = layout.findViewById(R.id.linear_layout_tombolas);

        backButton = layout.findViewById(R.id.back_button);
        newTombolaButton = layout.findViewById(R.id.button_new_tombola);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
        tombolasActivityViewModel.getAllTombolas().observe(this, new TombolaListChangedObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(view -> tombolasActivity.finish());
        newTombolaButton.setOnClickListener(view -> {

            tombolasActivityViewModel.selectTombola(new Tombola());
            tombolasActivity.switchToCreationStepOne();
        });
    }

    private class TombolaListChangedObserver implements Observer<List<Tombola>> {

        @Override
        public void onChanged(List<Tombola> tombolaList) {

            availableTombolas.removeAllViews();

            for(Tombola tombola : tombolaList) {

                long id = tombola.getId();

                TextView textView = (TextView) View.inflate(
                        tombolasActivity.getApplicationContext(), R.layout.list_element, null);

                textView.setText(tombola.toLabel());
                textView.setOnClickListener(new ShowDetailsListener());
                textView.setId((int) id);

                availableTombolas.addView(textView);
            }
        }
    }

    private class ShowDetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long tombolaId = textView.getId();
            tombolasActivityViewModel.selectTombola(tombolaId);

            tombolasActivity.switchToTombolaDetailsView();
        }
    }
}