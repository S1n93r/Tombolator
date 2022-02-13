package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.commons.NumberUtil;

import java.util.List;

public class MediaListFragment extends Fragment {

    private static final int ELEMENTS_PER_PAGE = 6;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);
    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private LinearLayout linearLayoutMedia;

    private Spinner mediaTypesSpinner;

    private TextView pageNumberCurrent;
    private TextView pageNumberMax;

    private ImageView sortButton;

    private Button backButton;
    private Button nextPageButton;
    private Button previousPageButton;
    private Button newMediaButton;

    private MediaListFragment() {}

    public static MediaListFragment newInstance() {
        return new MediaListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_list_fragment, container, false);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        mediaTypesSpinner = layout.findViewById(R.id.spinner_media_types);

        pageNumberCurrent = layout.findViewById(R.id.label_page_number_current);
        pageNumberMax = layout.findViewById(R.id.label_page_number_total);

        sortButton = layout.findViewById(R.id.button_sort_by);
        backButton = layout.findViewById(R.id.back_button);
        nextPageButton = layout.findViewById(R.id.button_next_page);
        previousPageButton = layout.findViewById(R.id.button_previous_page);
        newMediaButton = layout.findViewById(R.id.button_new_media);

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

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(this, new MediaListObserver());

        currentPage.removeObservers(this);

        currentPage.observe(this, new PageNumberCurrentObserver());

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(this, new PageNumberTotalObserver());
    }

    private void registerOnClickListener() {

        sortButton.setOnClickListener((View view) -> mediaActivityViewModel.toggleSorting());

        backButton.setOnClickListener(v -> mediaActivity.finish());

        nextPageButton.setOnClickListener((View view) -> {

            if(currentPage.getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if(mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if(currentPage.getValue() == MediaUtil.getTotalNumberOfPages(
                    mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue(), ELEMENTS_PER_PAGE))
                return;

            currentPage.setValue(currentPage.getValue() + 1);
        });

        previousPageButton.setOnClickListener((View view) -> {

            if(currentPage.getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if(currentPage.getValue() == 1)
                return;

            currentPage.setValue(currentPage.getValue() - 1);
        });

        newMediaButton.setOnClickListener((View view) -> {

            mediaActivityViewModel.selectMedia(new Media());

            mediaActivity.switchToCreationStepOne();
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

        linearLayoutMedia.removeAllViews();

        for(int i=start; i<end; i++) {

            Media media = mediaList.get(i);

            long id = media.getId();

            TextView textView = (TextView) View.inflate(
                    mediaActivity.getApplicationContext(), R.layout.list_element, null);

            String text = " " + media.toLabel();

            textView.setText(text);
            textView.setOnClickListener(showDetailsListener);
            textView.setId((int) id);

            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    MediaUtil.getMediaTypeIcon(media), 0, 0, 0);

            linearLayoutMedia.addView(textView);
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

    private class ShowDetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long mediaId = textView.getId();
            mediaActivityViewModel.selectMedia(mediaId);

            mediaActivity.switchToMediaDetailsView();
        }
    }

    private class MediaTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if(i == 0)
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