package com.example.tombolator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tombolator.config.ConfigActivity;
import com.example.tombolator.media.MediaActivity;
import com.example.tombolator.tombolas.TombolasActivity;

public class RootActivity extends AppCompatActivity {


    private ImageView tombolasButton;
    private ImageView mediaButton;
    private ImageView configButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.root_activity);

        tombolasButton = findViewById(R.id.button_tombolas);
        mediaButton = findViewById(R.id.button_media);
        configButton = findViewById(R.id.button_config);

        registerOnClickListener();
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