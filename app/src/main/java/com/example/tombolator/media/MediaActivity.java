package com.example.tombolator.media;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.example.tombolator.R;

import java.util.List;

public class MediaActivity extends AppCompatActivity {

    private MediaActivityModel viewModel;

    private LinearLayout linearLayoutMedia;

    private Button backButton;
    private Button newMediaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        linearLayoutMedia = findViewById(R.id.linear_layout_media);

        backButton = findViewById(R.id.button_back);
        newMediaButton = findViewById(R.id.button_new_media);



        viewModel = null;

        dataBind();

        registerOnClickListener();
    }

    private void dataBind() {

        viewModel.getMediaDatabase().observe(this, new MediaListObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newMediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MediaActivity.this, NewMediaActivity.class));
            }
        });
    }

    private class MediaListObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            for(Media media : mediaList){

                TextView textView = new TextView(getApplicationContext());
                textView.setText(media.toString());
                linearLayoutMedia.addView(textView);
            }
        }
    }
}