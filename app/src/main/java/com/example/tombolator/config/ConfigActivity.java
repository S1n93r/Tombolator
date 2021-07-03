package com.example.tombolator.config;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class ConfigActivity extends AppCompatActivity {

    private ConfigMainFragment configMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        configMainFragment = ConfigMainFragment.newInstance();

        setContentView(R.layout.media_activity);
        if (savedInstanceState == null) {
            switchToMainView();
        }
    }

    protected void switchToMainView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, configMainFragment)
                .commitNow();
    }
}