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

        /* TODO: Check if obsolete. */
        startTombolaFragment.setArguments(newTombolaFragment.getArguments());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, startTombolaFragment)
                .commitNow();
    }

    protected void switchToNewTombolaView() {

        /* TODO: Check if obsolete. */
        newTombolaFragment.setArguments(startTombolaFragment.getArguments());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newTombolaFragment)
                .commitNow();
    }
}