package com.example.tombolator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.config.ConfigActivity;
import com.example.tombolator.media.MediaActivity;
import com.example.tombolator.tombolas.TombolasActivity;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

public class RootActivity extends AppCompatActivity {

    private Button tombolasButton;
    private Button mediaButton;
    private Button configButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tryUpdate();

        setContentView(R.layout.root_activity);

        tombolasButton = findViewById(R.id.button_tombolas);
        mediaButton = findViewById(R.id.button_media);
        configButton = findViewById(R.id.button_config);

        registerOnClickListener();
    }

    private void tryUpdate() {

        String user = "S1n93r";
        String repo = "https://github.com/S1n93r/Tombolator";

        new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo(user, repo)
                .start();
    }

    private void registerOnClickListener() {

        tombolasButton.setOnClickListener(view -> startActivity(
                new Intent(RootActivity.this, TombolasActivity.class)));

        mediaButton.setOnClickListener(view -> startActivity(
                new Intent(RootActivity.this, MediaActivity.class)));

        configButton.setOnClickListener(view -> startActivity(
                new Intent(RootActivity.this, ConfigActivity.class)));
    }
}