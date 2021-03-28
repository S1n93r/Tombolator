package com.example.tombolator.config;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.tombolator.R;
import com.example.tombolator.SusisStashScript;
import com.example.tombolator.TomboDbApplication;

import java.util.Objects;

public class ConfigMainFragment extends Fragment {

    private final ConfigActivity parent;

    public static ConfigMainFragment newInstance(ConfigActivity parent) {
        return new ConfigMainFragment(parent);
    }

    private ConfigMainFragment(ConfigActivity parent) {
        this.parent = parent;
    }

    private Button resetMediaButton;
    private Button backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.config_main_fragment, container, false);

        resetMediaButton = layout.findViewById(R.id.button_reset_media);
        backButton = layout.findViewById(R.id.button_back);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        resetMediaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        new SusisStashScript(context).run();
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                parent.finish();
            }
        });
    }
}