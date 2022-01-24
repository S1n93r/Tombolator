package com.example.tombolator.tombolas;

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
import com.example.tombolator.media.Media;
import com.example.tombolator.utils.ToasterUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TombolaEditingFragment extends Fragment {

    TombolasActivity tombolasActivity;

    private TombolasActivityViewModel tombolaViewModel;

    private EditText nameValue;

    private LinearLayout availableTombolas;

    private Button backButton;
    private Button saveButton;

    private TombolaEditingFragment() {}

    public static TombolaEditingFragment newInstance() {
        return new TombolaEditingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolaViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombolas_edit_fragment, container, false);

        availableTombolas = layout.findViewById(R.id.linear_layout_tombolas);

        nameValue = layout.findViewById(R.id.name_value);

        backButton = layout.findViewById(R.id.back_button);
        saveButton = layout.findViewById(R.id.button_contiue);

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
    }

    private void registerObserver() {
        tombolaViewModel.getSelectedTombola().observe(this, new SelectedTombolaObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(v -> tombolasActivity.switchToTombolaDetailsView());

        saveButton.setOnClickListener((View view) -> {

            Tombola selectedTombola = tombolaViewModel.getSelectedTombola().getValue();
            Objects.requireNonNull(selectedTombola).setName(nameValue.getText().toString());

            tombolaViewModel.updateTombola(selectedTombola);

            tombolasActivity.switchToTombolaDetailsView();
        });
    }

    private void refreshMediaList() {

        Tombola selectedTombola = tombolaViewModel.getSelectedTombola().getValue();

        if(selectedTombola == null)
            return;

        availableTombolas.removeAllViews();

        for(Media media : selectedTombola.getAllMedia()) {

            View listElement = View.inflate(
                    tombolasActivity.getApplicationContext(), R.layout.media_in_tombola_list_element, null);

            TextView subTextView = listElement.findViewById(R.id.text_view_content);
            subTextView.setText(media.toLabel());
            subTextView.setId(media.getId().intValue());

            ImageView subImageView = listElement.findViewById(R.id.button_delete_media);
            subImageView.setOnClickListener(new RemoveMediaFromTombolaListener(media));

            availableTombolas.addView(listElement);
        }
    }

    private class SelectedTombolaObserver implements Observer<Tombola> {

        @Override
        public void onChanged(Tombola tombola) {

            nameValue.setText(tombola.getName());

            refreshMediaList();
        }
    }

    private class RemoveMediaFromTombolaListener implements View.OnClickListener {

        private final Media media;

        public RemoveMediaFromTombolaListener(Media media) {
            this.media = media;
        }

        @Override
        public void onClick(View view) {

            Tombola selectedTombola = tombolaViewModel.getSelectedTombola().getValue();
            Objects.requireNonNull(selectedTombola).removeMedia(media);

            tombolaViewModel.updateTombola(selectedTombola);

            refreshMediaList();

            ToasterUtil.makeShortToast(Objects.requireNonNull(getActivity()), getContext(),
                    "{0} wurde aus der Tombola entfernt.", media);
        }
    }
}