package com.example.tombolator.tombolas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private TombolaCreateNewFragment tombolaCreateNewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance(this);
        tombolaCreateNewFragment = TombolaCreateNewFragment.newInstance(this);

        setContentView(R.layout.activity_tombolas);
        if (savedInstanceState == null) {
            switchToStartView();
        }
    }

    protected void switchToStartView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaMainFragment)
                .commitNow();
    }

    protected void switchToNewTombolaView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaCreateNewFragment)
                .commitNow();
    }
}