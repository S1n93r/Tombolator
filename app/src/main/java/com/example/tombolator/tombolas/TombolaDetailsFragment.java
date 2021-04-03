package com.example.tombolator.tombolas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.DateUtil;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;
import com.example.tombolator.media.Media;

import java.util.Objects;

public class TombolaDetailsFragment extends Fragment {

    TombolasActivity tombolasActivity;

    private TombolasActivityViewModel tombolaViewModel;

    private TextView nameValue;
    private TextView createdAt;

    private LinearLayout availableMedia;

    private Button backButton;
    private Button drawButton;
    private Button deleteButton;

    private TombolaDetailsFragment() {}

    public static TombolaDetailsFragment newInstance() {
        return new TombolaDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolaViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombola_details_fragment, container, false);

        nameValue = layout.findViewById(R.id.name_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        availableMedia = layout.findViewById(R.id.linear_layout_available_media);

        backButton = layout.findViewById(R.id.button_back);
        drawButton = layout.findViewById(R.id.button_draw);
        deleteButton = layout.findViewById(R.id.button_delete);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
        tombolaViewModel.getSelectedTombola().observe(Objects.requireNonNull(this.getActivity()), new SelectedTombolaObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tombolasActivity.switchToTombolasMainView();
            }
        });

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tombola selectedTombola = tombolaViewModel.getSelectedTombola().getValue();

                Media drawnMedia = Objects.requireNonNull(selectedTombola).drawRandomMedia();

                DrawnMediaDialog drawnMediaDialog = new DrawnMediaDialog(Objects.requireNonNull(getContext()));
                drawnMediaDialog.show();
                drawnMediaDialog.getContent().setText(Objects.requireNonNull(drawnMedia).toLabel());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final Tombola tombola = tombolaViewModel.getSelectedTombola().getValue();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                .getApplicationContext());

                        final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                        tombolaDao.deleteMedia(tombola);
                    }
                });

                tombolaViewModel.removeTombola(Objects.requireNonNull(tombola).getId());

                tombolasActivity.switchToTombolasMainView();
            }
        });
    }

    private class SelectedTombolaObserver implements Observer<Tombola> {

        @Override
        public void onChanged(Tombola tombola) {

            nameValue.setText(tombola.getName());
            createdAt.setText(DateUtil.formatDate(tombola.getCreationTimestamp()));

            availableMedia.removeAllViews();

            for(Media media : tombola.getAllMedia()) {

                TextView textView = (TextView) View.inflate(
                        tombolasActivity.getApplicationContext(), R.layout.list_element, null);

                textView.setText(media.toLabel());
                textView.setId(media.getId().intValue());

                availableMedia.addView(textView);
            }
        }
    }
}