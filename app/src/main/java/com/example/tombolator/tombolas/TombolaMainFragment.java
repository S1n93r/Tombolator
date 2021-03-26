package com.example.tombolator.tombolas;

import android.os.AsyncTask;
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
import com.example.tombolator.TomboDbApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TombolaMainFragment extends Fragment {

    private final TombolasActivity parent;

    public static TombolaMainFragment newInstance(TombolasActivity parent) {
        return new TombolaMainFragment(parent);
    }

    private TombolaMainFragment(TombolasActivity parent) {
        this.parent = parent;
    }

    private TombolasActivityViewModel tombolasActivityViewModel;

    private LinearLayout linearLayoutTombolas;

    private Button backButton;
    private Button newTombolaButton;

    /* TODO: Helper for development. Will be replaced later. */
    private Button deleteAllButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_tombolas_main, container, false);

        linearLayoutTombolas = layout.findViewById(R.id.linear_layout_tombolas);

        backButton = layout.findViewById(R.id.button_back);
        newTombolaButton = layout.findViewById(R.id.button_new_tombola);
        deleteAllButton = layout.findViewById(R.id.buttonDeleteAll);

        registerObserver();
        registerOnClickListener();

        refreshViewModel();

        return layout;
    }

    private void registerObserver() {
        tombolasActivityViewModel.getTombolaDatabase()
                .observe(Objects.requireNonNull(this.getActivity()), new TombolasInsertedObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.finish();
            }
        });

        newTombolaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parent.switchToNewTombolaView();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                        tombolaDao.nukeTable();

                        tombolasActivityViewModel.removeAllTombolas();
                    }
                });
            }
        });
    }

    private void refreshViewModel() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                        .getApplicationContext());

                final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                List<Long> tombolaIds = tombolaDao.getAllIds();
                List<Tombola> tombolaList = new ArrayList<>();

                for (long id : tombolaIds) {
                    tombolaList.add(tombolaDao.getById(id));
                }

                tombolasActivityViewModel.addTombola(tombolaList);
            }
        });
    }

    private class TombolasInsertedObserver implements Observer<Map<Long, Tombola>> {

        @Override
        public void onChanged(Map<Long, Tombola> tombolaMap) {

            linearLayoutTombolas.removeAllViews();

            for(Map.Entry<Long, Tombola> pair : tombolaMap.entrySet()) {

                Tombola tombola = pair.getValue();

                long id = tombola.getId();
                String name = tombola.getName();

                String mediaString = "[" + id + "] " + name;

                TextView textView = new TextView(parent.getApplicationContext());
                textView.setTypeface(getResources().getFont(R.font.comic_sans_ms));
                textView.setTextSize(20);
                textView.setText(mediaString);
                textView.setOnClickListener(new ShowDetailsListener());
                textView.setId((int) id);

                linearLayoutTombolas.addView(textView);
            }
        }
    }

    private class ShowDetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long tombolaId = textView.getId();
            tombolasActivityViewModel.selectTombola(tombolaId);

            parent.switchToTombolaDetailsView();
        }
    }
}