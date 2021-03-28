package com.example.tombolator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.config.ConfigActivity;
import com.example.tombolator.media.MediaActivity;
import com.example.tombolator.tombolas.TombolasActivity;

public class MainActivity extends AppCompatActivity {

    private Button tombolasButton;
    private Button mediaButton;
    private Button configButton;

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

        tombolasButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TombolasActivity.class));
            }
        });

        mediaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MediaActivity.class));
            }
        });

        configButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ConfigActivity.class));
            }
        });
    }
}