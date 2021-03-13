package com.example.tombolator.media;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class NewMediaActivity extends AppCompatActivity {

    private TextView nameText;

    private Button saveButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_media);

        nameText = findViewById(R.id.text_name);

        saveButton = findViewById(R.id.button_save);
        backButton = findViewById(R.id.button_back);

        registerOnClickListener();
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Saving new media with name: " + nameText.getText());
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}