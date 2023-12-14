package com.example.tombolator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tombolator.config.ConfigActivity;
import com.example.tombolator.tombolas.TombolasActivity;

public class RootActivity extends AppCompatActivity {


    private ImageView tombolasButton;
    private ImageView configButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.root_activity);

        tombolasButton = findViewById(R.id.button_tombolas);
        configButton = findViewById(R.id.button_config);

        registerOnClickListener();
    }

    private void registerOnClickListener() {

        tombolasButton.setOnClickListener(view -> startActivity(
                new Intent(RootActivity.this, TombolasActivity.class)));

        configButton.setOnClickListener(view -> startActivity(
                new Intent(RootActivity.this, ConfigActivity.class)));
    }
}