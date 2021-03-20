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
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

public class NewTombolaFragment extends Fragment {

    TombolasActivity parent;

    public static NewTombolaFragment newInstance(TombolasActivity parent) {
        return new NewTombolaFragment(parent);
    }

    private NewTombolaFragment(TombolasActivity parent) {
        this.parent = parent;
    }

    private View layout;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private TextView editTextName;

    private Button saveButton;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        layout = inflater.inflate(R.layout.fragment_tombolas_new, container, false);

        editTextName = layout.findViewById(R.id.editTextName);

        saveButton = layout.findViewById(R.id.buttonSave);
        backButton = layout.findViewById(R.id.buttonBack);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextName.getText().length() > 0){

                    String name = editTextName.getText() != null ? editTextName.getText().toString() : "";

                    final Tombola tombola = new Tombola(name);

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            TomboDbApplication context = ((TomboDbApplication) getActivity().getApplicationContext());
                            final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                            tombolaDao.insertTombola(tombola);
                        }
                    });

                    resetForm();

                    parent.switchToStartView();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToStartView();
            }
        });
    }

    private void resetForm() {
        editTextName.setText("");
    }
}