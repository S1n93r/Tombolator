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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.commons.NumberUtil;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.media.MediaUtil;

import java.util.List;

public class TombolaCreationFragmentStepTwo extends Fragment {

    private static final int ELEMENTS_PER_PAGE = 6;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private TombolasActivity tombolasActivity;
    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private LinearLayout linearLayoutMedia;

    private Spinner mediaTypesSpinner;

    private TextView pageNumberCurrent;
    private TextView pageNumberMax;

    private ImageView sortButton;

    private Button nextPageButton;
    private Button previousPageButton;

    private Button backButton;
    private Button saveTombolaButton;

    private TombolaCreationFragmentStepTwo() {
    }

    public static TombolaCreationFragmentStepTwo newInstance() {
        return new TombolaCreationFragmentStepTwo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombolas_creation_fragment_step_two, container, false);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        mediaTypesSpinner = layout.findViewById(R.id.spinner_media_types);

        pageNumberCurrent = layout.findViewById(R.id.label_page_number_current);
        pageNumberMax = layout.findViewById(R.id.label_page_number_total);

        sortButton = layout.findViewById(R.id.button_sort_by);
        backButton = layout.findViewById(R.id.back_button);
        nextPageButton = layout.findViewById(R.id.button_next_page);
        previousPageButton = layout.findViewById(R.id.button_previous_page);
        saveTombolaButton = layout.findViewById(R.id.button_new_media);

        setUpMediaTypesSpinner();

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void setUpMediaTypesSpinner() {

        List<String> mediaTypesForSpinner = Media.MediaType.getMediaTypes();
        mediaTypesForSpinner.add(0, "Alle");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this.getActivity(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);
        mediaTypesSpinner.setAdapter(arrayAdapter);

        mediaTypesSpinner.setOnItemSelectedListener(new MediaTypeItemSelectedListener());
    }

    private void registerObserver() {

        if (tombolasActivityViewModel.getSelectedTombola() == null) {
            /* TODO: Throw log... Clonk! */
            throw new NullPointerException();
        }

        tombolasActivityViewModel.getSelectedTombola().observe(getViewLifecycleOwner(), new TombolaObserver());

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), new MediaListObserver());

        currentPage.removeObservers(this);

        currentPage.observe(getViewLifecycleOwner(), new PageNumberCurrentObserver());

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), new PageNumberTotalObserver());
    }

    private void registerOnClickListener() {

        sortButton.setOnClickListener((View view) -> mediaActivityViewModel.toggleSortingMode());

        backButton.setOnClickListener((View view) -> tombolasActivity.switchToCreationStepOne());

        saveTombolaButton.setOnClickListener((View view) -> {

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

    private class MediaTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (i == 0)
                mediaActivityViewModel.clearMediaType();
            else
                mediaActivityViewModel.selectMediaType(Media.MediaType.getMediaType(i - 1));

            currentPage.setValue(1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            /* TODO: When is this triggered? */
        }
    }
}