package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedListComponent;
import com.example.tombolator.commons.PaginatedListEntry;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateTombolaFragment extends Fragment {

    private final MutableLiveData<List<Media>> mediaCurrentTombola = new MutableLiveData<>();

    private TombolasActivity tombolasActivity;

    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private EditText nameEditText;

    private PaginatedListComponent<Media> paginatedMediaList;

    private ImageView saveButton;
    private ImageView addMediaButton;
    private ImageView backButton;

    private CreateTombolaFragment() {
    }

    public static CreateTombolaFragment newInstance() {
        return new CreateTombolaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View view = inflater.inflate(R.layout.create_tombola_fragment, container, false);

        nameEditText = view.findViewById(R.id.edit_text_name);

        paginatedMediaList = view.findViewById(R.id.paginated_media_list);

        backButton = view.findViewById(R.id.back_button);
        addMediaButton = view.findViewById(R.id.add_media_button);
        saveButton = view.findViewById(R.id.save_button);

        configurePaginatedTombolaList();
        registerOnClickListener();
        registerObserver();

        return view;
    }

    private void configurePaginatedTombolaList() {

        paginatedMediaList.setItemSortingStringConverter(Media::getName);

        paginatedMediaList.setItemToViewConverter(media -> {

            if (tombolasActivityViewModel.getSelectedTombola().getValue() == null)
                throw new IllegalStateException("Selected tombola should not be null!");

            @SuppressWarnings("unchecked")
            PaginatedListEntry<Media> paginatedListEntry = (PaginatedListEntry<Media>) View.inflate(
                    getContext(), R.layout.paginated_list_entry, null);

            paginatedListEntry.initialize(getViewLifecycleOwner());

            paginatedListEntry.setText(media.toLabel());
            paginatedListEntry.setId(media.getId().intValue());

            Set<Media> selectedMedia = new HashSet<>(tombolasActivityViewModel.getSelectedTombola().getValue().getAllMedia());

            if (selectedMedia.contains(media))
                paginatedListEntry.select();

            paginatedListEntry.getSelected().observe(getViewLifecycleOwner(), selected -> {

                Set<Media> selectedMediaLive = new HashSet<>(tombolasActivityViewModel.getSelectedTombola().getValue().getAllMedia());

                if (selected && !selectedMediaLive.contains(media))
                    tombolasActivityViewModel.getSelectedTombola().getValue().addMedia(media);
                else if (!selected)
                    tombolasActivityViewModel.getSelectedTombola().getValue().removeMedia(media);
            });

            return paginatedListEntry;
        });
    }

    private void registerObserver() {

        paginatedMediaList.setItems(getViewLifecycleOwner(), mediaCurrentTombola);

        tombolasActivityViewModel.getSelectedTombola().observe(getViewLifecycleOwner(), (Tombola tombola) -> {

            nameEditText.setText(tombola.getName());

            if (tombola.getAllMedia() != null)
                mediaCurrentTombola.setValue(tombola.getAllMedia());
        });

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), mediaList -> {
            if (mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData() != null)
                paginatedMediaList.setItems(getViewLifecycleOwner(), mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData());
        });
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(view -> {

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            /*  TODO: Insert media list content of this view here.*/

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolasMainView();
        });

        addMediaButton.setOnClickListener(view -> tombolasActivity.switchToMediaCreationView());

        backButton.setOnClickListener(view -> {
            resetForm();
            tombolasActivity.switchToTombolasMainView();
        });

        saveButton.setOnClickListener(new SaveTombolaListener());
    }

    private void resetForm() {
        nameEditText.getText().clear();
    }

    private class SaveTombolaListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (nameEditText.getText().length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_name_empty, Toast.LENGTH_LONG).show();
                return;
            }

            if (tombolasActivityViewModel.getSelectedTombola().getValue() == null) {
                /* TODO: Create log entry... Like a nice carving. */
                throw new NullPointerException();
            }

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            /* TODO: Type should be asked by spinner. */
            selectedTombola.setType(Tombola.Type.REUSE);
            selectedTombola.setName(nameEditText.getText().toString());

            /*  TODO: Insert media list content of this view here.*/

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolasMainView();
        }
    }
}