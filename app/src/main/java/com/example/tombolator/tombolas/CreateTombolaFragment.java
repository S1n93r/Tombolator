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

import java.util.List;

public class CreateTombolaFragment extends Fragment {

    private final MutableLiveData<List<Media>> mediaCurrentTombola = new MutableLiveData<>();

    private TombolasActivity tombolasActivity;

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

            PaginatedListEntry<Media> paginatedListEntry = new PaginatedListEntry<>(getContext());

            paginatedListEntry.initialize(getViewLifecycleOwner());

            paginatedListEntry.setText(media.toLabel());

            paginatedListEntry.setId(media.getId().intValue());

            paginatedListEntry.setClickTextRunnable(() -> {
                tombolasActivityViewModel.selectMedia(media);
                tombolasActivity.switchToCreateBook();
            });

            paginatedListEntry.setDeleteRunnable(() -> {

                List<Media> mediaList = mediaCurrentTombola.getValue();

                mediaList.remove(media);

                Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

                selectedTombola.removeMedia(media);

                mediaCurrentTombola.setValue(mediaList);
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
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(view -> {

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            /*  TODO: Insert media list content of this view here.*/

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolaList();
        });

        addMediaButton.setOnClickListener(view -> {

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            assert selectedTombola != null;

            String tombolaName = nameEditText.getText().toString();

            if (!tombolaName.isEmpty())
                selectedTombola.setName(tombolaName);

            long id = 0;

            for (Media media : selectedTombola.getAllMedia())
                if (media.getId() > id)
                    id = media.getId();

            Media newMedia = new Media();
            newMedia.setId(id + 1);

            tombolasActivityViewModel.selectMedia(newMedia);
            tombolasActivity.switchToChooseMedia();
        });

        backButton.setOnClickListener(view -> {
            resetForm();
            tombolasActivity.switchToTombolaList();
        });

        saveButton.setOnClickListener(new SaveTombolaListener());
    }

    private void resetForm() {
        nameEditText.getText().clear();
    }

    private class SaveTombolaListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String tombolaName = nameEditText.getText().toString();

            if (tombolaName.length() == 0) {
                Toast.makeText(getContext(), R.string.toast_media_name_empty, Toast.LENGTH_LONG).show();
                return;
            }

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            assert selectedTombola != null;

            /* TODO: Type should be asked by spinner. */
            selectedTombola.setType(Tombola.Type.REUSE);

            selectedTombola.setName(tombolaName);
            selectedTombola.setCreationTimestamp(System.currentTimeMillis());

            /*  TODO: Insert media list content of this view here.*/

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolaList();
        }
    }
}