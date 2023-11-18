package com.example.tombolator.tombolas;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedListComponent;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;

public class CreateTombolaFragment extends Fragment {

    private static final int FONT_COLOR_MEDIA_SELECTED = Color.parseColor("#3700B3");
    private static final int ELEMENTS_PER_PAGE = 6;
    /* TODO: Check what color is set here during runtime to fixate it here via code. */
    private static int FONT_COLOR_MEDIA = Color.BLACK;
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private TombolasActivity tombolasActivity;

    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private EditText nameEditText;

    private PaginatedListComponent<Media> paginatedTombolaList;

    private ImageView saveButton;
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

        View layout = inflater.inflate(R.layout.create_tombola_fragment, container, false);

        nameEditText = layout.findViewById(R.id.edit_text_name);

        paginatedTombolaList = layout.findViewById(R.id.paginated_media_list);

        backButton = layout.findViewById(R.id.back_button);
        saveButton = layout.findViewById(R.id.save_button);

        configurePaginatedTombolaList();
        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void configurePaginatedTombolaList() {

        paginatedTombolaList.setItemSortingStringConverter(Media::getName);

        paginatedTombolaList.setItemToViewConverter(media -> {

            /* FIXME: Already selected media entries are not marked when switching pages or editing a tombola. */
            TextView textView = (TextView) View.inflate(
                    tombolasActivity.getApplicationContext(), R.layout.list_element, null);

            textView.setText(media.toLabel());
            textView.setOnClickListener(new SelectMediaListener(media));
            textView.setId(media.getId().intValue());

            return textView;
        });
    }

    private void registerObserver() {

        tombolasActivityViewModel.getSelectedTombola().observe(getViewLifecycleOwner(),
                (Tombola tombola) -> nameEditText.setText(tombola.getName()));

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), mediaList -> {
            if (mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData() != null)
                paginatedTombolaList.setItems(getViewLifecycleOwner(), mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData());
        });
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener((View view) -> {

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            /*  TODO: Insert media list content of this view here.*/

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolasMainView();
        });


        backButton.setOnClickListener(v -> {
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

    private class SelectMediaListener implements View.OnClickListener {

        private final Media selectedMedia;

        public SelectMediaListener(Media selectedMedia) {
            this.selectedMedia = selectedMedia;
        }

        @Override
        public void onClick(View v) {

            if (tombolasActivityViewModel.getSelectedTombola().getValue() == null)
                throw new IllegalStateException("Selected tombola should never be null!");

            TextView textView = (TextView) v;

            Typeface defaultTypeface = textView.getTypeface();

            int defaultTextColor = textView.getCurrentTextColor();

            if (defaultTextColor != FONT_COLOR_MEDIA_SELECTED)
                FONT_COLOR_MEDIA = defaultTextColor;

            if (tombolasActivityViewModel.getSelectedTombola().getValue().getAllMedia().contains(selectedMedia)) {

                tombolasActivityViewModel.getSelectedTombola().getValue().removeMedia(selectedMedia);
                textView.setTextColor(FONT_COLOR_MEDIA);
                textView.setTypeface(defaultTypeface, Typeface.NORMAL);
            } else {

                tombolasActivityViewModel.getSelectedTombola().getValue().addMedia(selectedMedia);
                textView.setTextColor(FONT_COLOR_MEDIA_SELECTED);
                textView.setTypeface(defaultTypeface, Typeface.BOLD);
            }
        }
    }
}