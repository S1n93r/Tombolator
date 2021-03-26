package com.example.tombolator.tombolas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

import java.util.Objects;

public class TombolaDetailsFragment extends Fragment {

    TombolasActivity parent;

    public static TombolaDetailsFragment newInstance(TombolasActivity parent) {
        return new TombolaDetailsFragment(parent);
    }

    private TombolaDetailsFragment(TombolasActivity parent) {
        this.parent = parent;
    }

    private TombolasActivityViewModel tombolaViewModel;

    private TextView nameValue;
    private TextView createdAt;

    private Button backButton;
    private Button deleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolaViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_tombola_details, container, false);

        nameValue = layout.findViewById(R.id.name_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        backButton = layout.findViewById(R.id.button_back);
        deleteButton = layout.findViewById(R.id.button_delete);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
        tombolaViewModel.getSelectedTombola().observe(Objects.requireNonNull(this.getActivity()), new SelectedMediaObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToMainView();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Tombola tombola = tombolaViewModel.getSelectedTombola().getValue();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                        tombolaDao.deleteMedia(tombola);
                    }
                });

                tombolaViewModel.removeTombola(Objects.requireNonNull(tombola).getId());

                parent.switchToMainView();
            }
        });
    }

    private class SelectedMediaObserver implements Observer<Tombola> {

        @Override
        public void onChanged(Tombola tombola) {

            nameValue.setText(tombola.getName());
            createdAt.setText(String.valueOf(tombola.getCreationTimestamp()));
        }
    }
}