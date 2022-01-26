package com.example.tombolator.tombolas;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.commons.CustomAlertDialog;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.tombolas.drawing.DrawDialog;
import com.example.tombolator.tombolas.drawing.DrawFialogFactory;
import com.example.tombolator.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TombolaDetailsFragment extends Fragment {

    TombolasActivity tombolasActivity;

    private TombolasActivityViewModel tombolaViewModel;
    private MediaActivityViewModel mediaActivityViewModel;

    private TextView nameValue;
    private TextView createdAt;
    private TextView numberOfMediaAll;
    private TextView numberOfMediaAvailable;
    private TextView numberOfMediaDrawn;

    private Spinner tombolaTypeSpinner;

    private Button backButton;
    private Button drawButton;
    private Button editTombolaButton;
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
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombolas_details_fragment, container, false);

        nameValue = layout.findViewById(R.id.name_value);
        createdAt = layout.findViewById(R.id.created_at_value);
        numberOfMediaAll = layout.findViewById(R.id.media_all_value);
        numberOfMediaAvailable = layout.findViewById(R.id.media_available_value);
        numberOfMediaDrawn = layout.findViewById(R.id.media_drawn_value);

        tombolaTypeSpinner = layout.findViewById(R.id.spinner_tombola_type);

        backButton = layout.findViewById(R.id.back_button);
        drawButton = layout.findViewById(R.id.button_draw);
        editTombolaButton = layout.findViewById(R.id.button_edit_tombola);
        deleteButton = layout.findViewById(R.id.button_delete);

        registerObserver();
        registerOnClickListener();
        setUpSpinner();

        return layout;
    }

    private void setUpSpinner() {

        if(tombolaViewModel.getSelectedTombola().getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        List<String> typesAsStrings = new ArrayList<>();

        for(Tombola.Type type : Tombola.Type.values()) {
            typesAsStrings.add(type.description);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, typesAsStrings);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_element);

        tombolaTypeSpinner.setAdapter(arrayAdapter);

        tombolaTypeSpinner.setSelection(tombolaViewModel.getSelectedTombola().getValue().getType().ordinal());

        tombolaTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(view == null)
                    return;

                Tombola selectedTombola = tombolaViewModel.getSelectedTombola().getValue();
                selectedTombola.setType(Tombola.Type.values()[i]);

                tombolaViewModel.updateTombola(selectedTombola);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /* TODO: When does this trigger? */
            }
        });
    }

    private void registerObserver() {
        tombolaViewModel.getSelectedTombola().observe(this, new SelectedTombolaObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View view) -> tombolasActivity.switchToTombolasMainView());

        drawButton.setOnClickListener((View view) -> {

            Tombola selectedTombola = tombolaViewModel.getSelectedTombola().getValue();

            Media drawnMedia = Objects.requireNonNull(selectedTombola).drawRandomMedia();

            /* TODO: A little bite hacky. Maybe I can find something better? */
            if(selectedTombola.getType().equals(Tombola.Type.DELETE))
                mediaActivityViewModel.delete(drawnMedia);

            Context context = getContext();

            if(context == null)
                throw new NullPointerException();

            DrawDialog drawDialog = DrawFialogFactory.createRandomDialog(context);

            /* Has to be called before setContent() and setIcon() so onCreate() was fired*/
            drawDialog.show();

            if(drawnMedia == null) {
                drawDialog.getContentText().setText(R.string.draw_media_on_empty_tombola);
                return;
            }

            drawDialog.setIcon(drawnMedia);
            drawDialog.getContentText().setText(Objects.requireNonNull(drawnMedia).toLabel());

            updateCounters(selectedTombola);

            tombolaViewModel.insertTombola(selectedTombola);
        });

        editTombolaButton.setOnClickListener((View view) -> tombolasActivity.switchToCreationStepOne());

        deleteButton.setOnClickListener((View view) -> {

            if(getActivity() == null) {
                /* TODO: Throw log entry. */
                throw new NullPointerException();
            }

            Tombola tombola = tombolaViewModel.getSelectedTombola().getValue();

            CustomAlertDialog customAlert = new CustomAlertDialog(getActivity());

            customAlert.setTitle("Tombola löschen");
            customAlert.setMessage("Möchtest du die Tombola \"" + tombola.getName() + "\" wirklich löschen?");

            customAlert.setOnAccepted(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    tombolaViewModel.deleteTombola(tombola);
                    tombolasActivity.switchToTombolasMainView();
                    customAlert.dismiss();
                }
            });

            customAlert.setOnDeny(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    customAlert.dismiss();
                }
            });

            customAlert.show();
        });
    }

    private void updateCounters(Tombola tombola) {

        numberOfMediaAll.setText(String.valueOf(tombola.getAllMedia().size()));
        numberOfMediaAvailable.setText(String.valueOf(tombola.getMediaAvailable().size()));
        numberOfMediaDrawn.setText(String.valueOf(tombola.getMediaDrawn().size()));
    }

    private class SelectedTombolaObserver implements Observer<Tombola> {

        @Override
        public void onChanged(Tombola tombola) {

            if(tombolaViewModel.getSelectedTombola().getValue() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            nameValue.setText(tombola.getName());
            createdAt.setText(DateUtil.formatDate(tombola.getCreationTimestamp()));
            numberOfMediaAll.setText(String.valueOf(tombola.getAllMedia().size()));
            numberOfMediaAvailable.setText(String.valueOf(tombola.getMediaAvailable().size()));
            numberOfMediaDrawn.setText(String.valueOf(tombola.getMediaDrawn().size()));

            tombolaTypeSpinner.setEnabled(false);
            tombolaTypeSpinner.setSelection(tombolaViewModel.getSelectedTombola().getValue().getType().ordinal());
            tombolaTypeSpinner.setEnabled(true);
        }
    }
}