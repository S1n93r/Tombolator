package com.example.tombolator.tombolas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private TombolaCreationFragment tombolaCreationFragment;
    private TombolaDetailsFragment tombolaDetailsFragment;
    private TombolaEditingFragment tombolaEditingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance();
        tombolaCreationFragment = TombolaCreationFragment.newInstance();
        tombolaDetailsFragment = TombolaDetailsFragment.newInstance();
        tombolaEditingFragment = TombolaEditingFragment.newInstance();

        setContentView(R.layout.tombolas_activity);
        if (savedInstanceState == null) {
            switchToTombolasMainView();
        }
    }

    protected void switchToTombolasMainView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaMainFragment)
                .commitNow();
    }

    protected void switchToCreation() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaCreationFragment)
                .commitNow();
    }

    protected void switchToTombolaDetailsView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaDetailsFragment)
                .commitNow();
    }

    protected void switchToTombolEditingView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaEditingFragment)
                .commitNow();
    }
}