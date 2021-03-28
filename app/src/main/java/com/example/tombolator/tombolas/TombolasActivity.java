package com.example.tombolator.tombolas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private TombolaCreateNewFragment tombolaCreateNewFragment;
    private TombolaDetailsFragment tombolaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance(this);
        tombolaCreateNewFragment = TombolaCreateNewFragment.newInstance(this);
        tombolaDetailsFragment = TombolaDetailsFragment.newInstance(this);

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

    protected void switchToNewTombolaView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaCreateNewFragment)
                .commitNow();
    }

    protected void switchToTombolaDetailsView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaDetailsFragment)
                .commitNow();
    }
}