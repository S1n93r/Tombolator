package com.example.tombolator.media;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.DateUtil;
import com.example.tombolator.R;
import com.example.tombolator.ToasterUtil;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolasActivityViewModel;

import java.util.List;
import java.util.Objects;

public class MediaDetailsFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private TextView idValue;
    private TextView nameValue;
    private TextView numberValue;
    private TextView titleValue;
    private TextView typeValue;
    private TextView createdAt;

    private Button editMediaButton;
    private Button backButton;
    private Button deleteButton;
    private Button addToTombola;

    private Spinner tombolaSpinner;

    public static MediaDetailsFragment newInstance() {
        return new MediaDetailsFragment();
    }

    private MediaDetailsFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_details_fragment, container, false);

        idValue = layout.findViewById(R.id.id_value);
        nameValue = layout.findViewById(R.id.name_value);
        numberValue = layout.findViewById(R.id.number_value);
        titleValue = layout.findViewById(R.id.title_value);
        typeValue = layout.findViewById(R.id.type_value);
        createdAt = layout.findViewById(R.id.created_at_value);

        editMediaButton = layout.findViewById(R.id.button_edit_media);
        backButton = layout.findViewById(R.id.button_back);
        deleteButton = layout.findViewById(R.id.button_delete);
        addToTombola = layout.findViewById(R.id.button_add_to_tombola);

        tombolaSpinner = layout.findViewById(R.id.tombola_spinner);

        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void registerObserver() {

        mediaActivityViewModel.getSelectedMedia().observe(this, new SelectedMediaObserver());
        tombolasActivityViewModel.getAllTombolas().observe(this, new TombolaListObserver());
    }

    private void registerOnClickListener() {

        editMediaButton.setOnClickListener((View v) -> mediaActivity.switchToCreationStepOne());

        addToTombola.setOnClickListener((View view) -> saveToTombola());

        backButton.setOnClickListener((View v) -> mediaActivity.switchToMediaListStepTwo());

        deleteButton.setOnClickListener((View v) -> deleteMedia());
    }

    private void deleteMedia() {

        Media media = mediaActivityViewModel.getSelectedMedia().getValue();

        if(media == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        tombolasActivityViewModel.deleteMediaFromAllTombolas(media);
        mediaActivityViewModel.delete(media);

        mediaActivity.switchToMediaListStepTwo();
    }

    private void saveToTombola() {

        Tombola selectedTombola = (Tombola) tombolaSpinner.getSelectedItem();

        selectedTombola.addMedia(mediaActivityViewModel.getSelectedMedia().getValue());

        tombolasActivityViewModel.updateTombola(selectedTombola);

        ToasterUtil.makeShortToast(Objects.requireNonNull(getActivity()), getContext(),
                "Medium wurde zu Tombola {0} hinzugefügt.", selectedTombola.getName());
    }

    private class SelectedMediaObserver implements Observer<Media> {

        @Override
        public void onChanged(Media media) {

            idValue.setText(String.valueOf(media.getId()));
            nameValue.setText(media.getName());
            numberValue.setText(String.valueOf(media.getNumber()));
            titleValue.setText(media.getTitle());
            typeValue.setText(media.getMediaType());
            createdAt.setText(DateUtil.formatDate(media.getCreationTimestamp()));
        }
    }

    private class TombolaListObserver implements Observer<List<Tombola>> {

        @Override
        public void onChanged(List<Tombola> tombolaList) {

            TombolaSpinnerItemAdapter tombolaArrayAdapter = new TombolaSpinnerItemAdapter(
                    Objects.requireNonNull(getContext()), tombolaList);

            tombolaSpinner.setAdapter(tombolaArrayAdapter);
        }
    }

    private class TombolaSpinnerItemAdapter extends BaseAdapter {

        private final Context context;
        private final List<Tombola> tombolaList;

        private final LayoutInflater inflater;

        public TombolaSpinnerItemAdapter(Context context, List<Tombola> tombolaList) {

            this.context = context;
            this.tombolaList = tombolaList;

            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return tombolaList.size();
        }

        @Override
        public Object getItem(int i) {
            return tombolaList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View rowView = inflater.inflate(R.layout.tombola_spinner_item, viewGroup, false);

            TextView subTextView = rowView.findViewById(R.id.text_view_content);

            subTextView.setText(tombolaList.get(i).getName());

            return rowView;
        }
    }
}