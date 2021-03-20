package com.example.tombolator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.media.MediaActivity;
import com.example.tombolator.tombolas.TombolasActivity;

public class MainActivity extends AppCompatActivity {

    private Button tombolasButton;
    private Button mediaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tombolasButton = findViewById(R.id.buttonTombolas);
        mediaButton = findViewById(R.id.buttonMedia);

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
    }
}