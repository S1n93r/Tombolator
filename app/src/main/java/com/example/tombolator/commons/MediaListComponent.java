package com.example.tombolator.commons;

import android.content.Context;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaUtil;

import java.util.List;
import java.util.Locale;

public class MediaListComponent extends ConstraintLayout {

    private static final int ELEMENTS_PER_PAGE = 6;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);
    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private final LinearLayout mediaListLinearLayout = new LinearLayout(getContext());

    private final Spinner mediaTypesSpinner = new Spinner(getContext());

    private final TextView pageNumberCurrent = new TextView(getContext());
    private final TextView pageNumberMax = new TextView(getContext());

    private final ImageView sortButton = new ImageView(getContext());

    private final Button previousPageButton = new Button(getContext());

    public MediaListComponent(@NonNull Context context) {

        super(context);

        setUpMediaTypesSpinner();

        registerObserver();
        registerOnClickListener();
    }

    private static String formatNumberFullDigitsLeadingZero(int number) {

        int numberOfDigits = String.valueOf(number).length();

        String numberFormat = "%0" + numberOfDigits + "d";

        return String.format(Locale.getDefault(), numberFormat, number);
    }

    private void setUpMediaTypesSpinner() {

        List<String> mediaTypesForSpinner = Media.MediaType.getMediaTypes();
        mediaTypesForSpinner.add(0, "Alle");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);
        mediaTypesSpinner.setAdapter(arrayAdapter);

        mediaTypesSpinner.setOnItemSelectedListener(new MediaTypeItemSelectedListener());
    }

    private void registerObserver() {

        /* TODO: Make  MediaListObserver observe generic filtered and sorted Media list. */
        //mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(this, new MediaListFragment.MediaListObserver());

        /* TODO: Who can own this? Normal observe()? */
        //currentPage.removeObservers(this);

        /* TODO: Who can own this? Normal observe()? */
        //currentPage.observe(this, new PageNumberCurrentObserver());

        /* TODO: Make  PageNumberTotalObserver observe generic filtered and sorted Media list. */
        //mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(this, new MediaListFragment.PageNumberTotalObserver());
    }

    private void registerOnClickListener() {

        /* TODO: Implement toggleSorting() here. */
        //sortButton.setOnClickListener((View view) -> mediaActivityViewModel.toggleSorting());

        previousPageButton.setOnClickListener((View view) -> {

            if(currentPage.getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if(currentPage.getValue() == 1)
                return;

            currentPage.setValue(currentPage.getValue() - 1);
        });
    }

    private void showMediaOnCurrentPage (List<Media> mediaList) {

        if(currentPage.getValue() == null) {
            /* TODO: Log NPE here. */
            throw new NullPointerException();
        }

        int start = (currentPage.getValue() - 1) * ELEMENTS_PER_PAGE;
        int end = start + ELEMENTS_PER_PAGE;

        if(end > mediaList.size())
            end = mediaList.size();

        mediaListLinearLayout.removeAllViews();

        for(int i=start; i<end; i++) {

            Media media = mediaList.get(i);

            long id = media.getId();

            TextView textView = (TextView) View.inflate(getContext(), R.layout.list_element, null);

            String text = " " + media.toLabel();

            textView.setText(text);
            textView.setOnClickListener(showDetailsListener);
            textView.setId((int) id);

            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    MediaUtil.getMediaTypeIcon(media), 0, 0, 0);

            mediaListLinearLayout.addView(textView);
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

            pageNumberCurrent.setText(formatNumberFullDigitsLeadingZero(pageNumber));

            /* TODO: Use own media list here instead from viewModel. */
            //showMediaOnCurrentPage(mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue());
        }
    }

    private class PageNumberTotalObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            int numberOfPages = MediaUtil.getTotalNumberOfPages(mediaList, ELEMENTS_PER_PAGE);

            pageNumberMax.setText(formatNumberFullDigitsLeadingZero(numberOfPages));
        }
    }

    private class ShowDetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long mediaId = textView.getId();

            /* TODO: Use own selectedMedia */
            //mediaActivityViewModel.selectMedia(mediaId);
        }
    }

    private class MediaTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if(i == 0) {
                /* TODO: Use own clearMediaType */
                //mediaActivityViewModel.clearMediaType();
            } else {
                /* TODO: Use own selectMediaType */
                //mediaActivityViewModel.selectMediaType(Media.MediaType.getMediaType(i - 1));
            }

            currentPage.setValue(1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            /* TODO: When is this triggered? */
        }
    }
}
