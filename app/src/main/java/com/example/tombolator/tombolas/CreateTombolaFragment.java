package com.example.tombolator.tombolas;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.commons.NumberUtil;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.media.MediaType;
import com.example.tombolator.media.MediaUtil;

import java.util.Arrays;
import java.util.List;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class CreateTombolaFragment extends Fragment {

    private static final int ELEMENTS_PER_PAGE = 6;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private TombolasActivity tombolasActivity;

    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private EditText nameEditText;

    /* Media control*/
    private ImageView sortButton;
    private Spinner mediaTypesSpinner;

    private LinearLayout linearLayoutMedia;

    /* Pagination */
    private TextView pageNumberCurrent;
    private TextView pageNumberMax;
    private Button nextPageButton;
    private Button previousPageButton;

    /* Main actions */
    private Button saveButton;
    private Button backButton;

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

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        sortButton = layout.findViewById(R.id.button_sort_by);
        mediaTypesSpinner = layout.findViewById(R.id.spinner_media_types);

        pageNumberCurrent = layout.findViewById(R.id.label_page_number_current);
        pageNumberMax = layout.findViewById(R.id.label_page_number_total);
        nextPageButton = layout.findViewById(R.id.button_next_page);
        previousPageButton = layout.findViewById(R.id.button_previous_page);

        backButton = layout.findViewById(R.id.back_button);
        saveButton = layout.findViewById(R.id.save_button);

        setUpMediaTypesSpinner();

        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void registerObserver() {

        tombolasActivityViewModel.getSelectedTombola().observe(getViewLifecycleOwner(), new TombolaObserver());

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), new MediaListObserver());

        currentPage.removeObservers(this);

        currentPage.observe(getViewLifecycleOwner(), new PageNumberCurrentObserver());

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), new PageNumberTotalObserver());

        tombolasActivityViewModel.getSelectedTombola().observe(getViewLifecycleOwner(),
                (Tombola tombola) -> nameEditText.setText(tombola.getName()));
    }

    private void setUpMediaTypesSpinner() {

        List<String> mediaTypesForSpinner = StreamSupport.stream(Arrays.asList(MediaType.values()))
                .map(MediaType::getCleanName)
                .collect(Collectors.toList());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);
        mediaTypesSpinner.setAdapter(arrayAdapter);
    }


    private void registerOnClickListener() {

        sortButton.setOnClickListener((View view) -> mediaActivityViewModel.toggleSortingMode());

        saveButton.setOnClickListener((View view) -> {

            Tombola selectedTombola = tombolasActivityViewModel.getSelectedTombola().getValue();

            /*  TODO: Insert media list content of this view here.*/

            tombolasActivityViewModel.insertTombola(selectedTombola);

            tombolasActivity.switchToTombolasMainView();
        });

        nextPageButton.setOnClickListener((View view) -> {

            if (currentPage.getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if (mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if (currentPage.getValue() == MediaUtil.getTotalNumberOfPages(
                    mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue(), ELEMENTS_PER_PAGE))
                return;

            currentPage.setValue(currentPage.getValue() + 1);
        });

        previousPageButton.setOnClickListener((View view) -> {

            if (currentPage.getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if (currentPage.getValue() == 1)
                return;

            currentPage.setValue(currentPage.getValue() - 1);
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

    private void showMediaOnCurrentPage(List<Media> mediaList) {

        if (tombolasActivityViewModel.getSelectedTombola().getValue() == null) {
            /* TODO: Log NPE here. */
            throw new NullPointerException();
        }

        if (currentPage.getValue() == null) {
            /* TODO: Log NPE here. */
            throw new NullPointerException();
        }

        int start = (currentPage.getValue() - 1) * ELEMENTS_PER_PAGE;
        int end = start + ELEMENTS_PER_PAGE;

        if (end > mediaList.size())
            end = mediaList.size();

        linearLayoutMedia.removeAllViews();

        for (int i = start; i < end; i++) {

            Media media = mediaList.get(i);

            long id = media.getId();

            TextView textView = (TextView) View.inflate(
                    tombolasActivity.getApplicationContext(), R.layout.list_element, null);

            String text = " " + media.toLabel();

            Typeface defaultTypeface = textView.getTypeface();
            int defaultTextColor = textView.getCurrentTextColor();

            for (Media mediaInTombola : tombolasActivityViewModel.getSelectedTombola().getValue().getAllMedia()) {

                if (id == mediaInTombola.getId()) {

                    textView.setTextColor(Color.parseColor("#3700B3"));
                    textView.setTypeface(defaultTypeface, Typeface.BOLD);
                }
            }

            textView.setText(text);
            textView.setOnClickListener((View view) -> {

                if (tombolasActivityViewModel.getSelectedTombola().getValue().getAllMedia().contains(media)) {

                    tombolasActivityViewModel.getSelectedTombola().getValue().removeMedia(media);
                    textView.setTextColor(defaultTextColor);
                    textView.setTypeface(defaultTypeface);
                } else {

                    tombolasActivityViewModel.getSelectedTombola().getValue().addMedia(media);
                    textView.setTextColor(Color.parseColor("#3700B3"));
                    textView.setTypeface(defaultTypeface, Typeface.BOLD);
                }

            });
            textView.setId((int) id);

            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    MediaUtil.getMediaTypeIcon(media), 0, 0, 0);

            linearLayoutMedia.addView(textView);
        }
    }

    private class MediaTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (i == 0)
                mediaActivityViewModel.clearMediaType();
            else
                mediaActivityViewModel.selectMediaType(MediaType.values()[i]);

            currentPage.setValue(1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            /* TODO: When is this triggered? */
        }
    }

    private class TombolaObserver implements Observer<Tombola> {

        @Override
        public void onChanged(Tombola tombola) {
            showMediaOnCurrentPage(mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue());
        }
    }

    private class MediaListObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {
            showMediaOnCurrentPage(mediaList);
        }
    }

    private class PageNumberCurrentObserver implements Observer<Integer> {

        @Override
        public void onChanged(Integer pageNumber) {

            pageNumberCurrent.setText(NumberUtil.formatNumberFullDigitsLeadingZero(pageNumber));
            showMediaOnCurrentPage(mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue());
        }
    }

    private class PageNumberTotalObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            int numberOfPages = MediaUtil.getTotalNumberOfPages(mediaList, ELEMENTS_PER_PAGE);

            pageNumberMax.setText(NumberUtil.formatNumberFullDigitsLeadingZero(numberOfPages));
        }
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