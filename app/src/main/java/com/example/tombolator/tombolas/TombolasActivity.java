package com.example.tombolator.tombolas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private StartTombolaFragment startTombolaFragment;
    private NewTombolaFragment newTombolaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        startTombolaFragment = StartTombolaFragment.newInstance(this);
        newTombolaFragment = NewTombolaFragment.newInstance(this);

        setContentView(R.layout.activity_tombolas);
        if (savedInstanceState == null) {
            switchToStartView();
        }
    }

    protected void switchToStartView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, startTombolaFragment)
                .commitNow();
    }

    protected void switchToNewTombolaView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newTombolaFragment)
                .commitNow();
    }
}