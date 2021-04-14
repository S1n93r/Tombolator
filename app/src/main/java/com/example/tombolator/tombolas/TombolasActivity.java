package com.example.tombolator.tombolas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tombolator.R;

public class TombolasActivity extends AppCompatActivity {

    private TombolaMainFragment tombolaMainFragment;
    private TombolaCreationStepOneFragment tombolaCreationStepOneFragment;
    private TombolaCreationStepTwoFragment tombolaCreationStepTwoFragment;
    private TombolaDetailsFragment tombolaDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        tombolaMainFragment = TombolaMainFragment.newInstance();
        tombolaCreationStepOneFragment = TombolaCreationStepOneFragment.newInstance();
        tombolaCreationStepTwoFragment = TombolaCreationStepTwoFragment.newInstance();
        tombolaDetailsFragment = TombolaDetailsFragment.newInstance();

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

    protected void switchToCreationStepOne() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaCreationStepOneFragment)
                .commitNow();
    }

    protected void switchToCreationStepTwo() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaCreationStepTwoFragment)
                .commitNow();
    }

    protected void switchToTombolaDetailsView() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, tombolaDetailsFragment)
                .commitNow();
    }
}