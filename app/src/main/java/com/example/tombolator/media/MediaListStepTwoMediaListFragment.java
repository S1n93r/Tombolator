package com.example.tombolator.media;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MediaListStepTwoMediaListFragment extends Fragment {

    private static final int ELEMENTS_PER_PAGE = 8;
    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private MediaActivity mediaActivity;
    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private MediaActivityViewModel mediaActivityViewModel;

    private EditText search;

    private LinearLayout linearLayoutMedia;

    private TextView pageNumberCurrent;
    private TextView pageNumberMax;

    private ImageView sortButton;
    private Button backButton;
    private Button nextPageButton;
    private Button previousPageButton;
    private Button newMediaButton;

    private MediaListStepTwoMediaListFragment() {}

    public static MediaListStepTwoMediaListFragment newInstance() {
        return new MediaListStepTwoMediaListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_list_step_two_media_list, container, false);

        search = layout.findViewById(R.id.edit_text_search);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        pageNumberCurrent = layout.findViewById(R.id.label_page_number_current);
        pageNumberMax = layout.findViewById(R.id.label_page_number_total);

        sortButton = layout.findViewById(R.id.button_sort_by);
        backButton = layout.findViewById(R.id.button_back);
        nextPageButton = layout.findViewById(R.id.button_next_page);
        previousPageButton = layout.findViewById(R.id.button_previous_page);
        newMediaButton = layout.findViewById(R.id.button_new_media);

        registerObserver();
        registerOnKeyListener();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaListObserver());

        currentPage.observe(this.getActivity(), new PageNumberCurrentObserver());

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(this.getActivity(), new PageNumberTotalObserver());

        mediaActivityViewModel.getSelectedMediaTypes().observe(this.getActivity(), new SelectedMediaTypesObserver());
    }

    private void registerOnKeyListener() {
        search.setOnKeyListener(new SearchMediaListener());
    }

    private void registerOnClickListener() {

        sortButton.setOnClickListener((View view) -> mediaActivityViewModel.toggleSorting());

        backButton.setOnClickListener((View view) -> mediaActivity.switchToMediaListStepOne());

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

            currentPage.postValue(currentPage.getValue() + 1);
        });

        previousPageButton.setOnClickListener((View view) -> {

            if(currentPage.getValue() == null) {
                /* TODO: Log NPE here. */
                throw new NullPointerException();
            }

            if(currentPage.getValue() == 1)
                return;

            currentPage.postValue(currentPage.getValue() - 1);
        });

        newMediaButton.setOnClickListener((View view) -> {

            Media media = new Media();

            mediaActivityViewModel.selectMedia(media);

            mediaActivity.switchToCreationStepOne();
        });
    }

    private class SelectedMediaTypesObserver implements Observer<List<String>> {

        @Override
        public void onChanged(List<String> mediaTypes) {
            currentPage.postValue(1);
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
            showMediaOnCurrentPage(mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().getValue());
        }
    }

    private class PageNumberTotalObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            int numberOfPages = MediaUtil.getTotalNumberOfPages(mediaList, ELEMENTS_PER_PAGE);

            pageNumberMax.setText(formatNumberFullDigitsLeadingZero(numberOfPages));
        }
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
                    MediaUtil.getMediaIcon(media), 0, 0, 0);

            linearLayoutMedia.addView(textView);
        }
    }

    private static String formatNumberFullDigitsLeadingZero(int number) {

        int numberOfDigits = String.valueOf(number).length();

        String numberFormat = "%0" + numberOfDigits + "d";

        return String.format(Locale.getDefault(), numberFormat, number);
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

    private class SearchMediaListener implements View.OnKeyListener {

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                String searchFilter = search.getText() != null ? search.getText().toString() : "";
                mediaActivityViewModel.setMediaSearchFilter(searchFilter);
            }

            return false;
        }
    }
}