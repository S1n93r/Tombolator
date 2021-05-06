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
    private MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private MediaActivity mediaActivity;
    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private MediaActivityViewModel mediaActivityViewModel;

    private EditText search;

    private LinearLayout linearLayoutMedia;

    private TextView pageNumberLabel;

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

        pageNumberLabel = layout.findViewById(R.id.label_page_number);

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

        mediaActivityViewModel.getAllMedia()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaInsertedObserver());

        mediaActivityViewModel.getCurrentPage().observe(this.getActivity(), new PageChangedObserver());

        currentPage.observe(this.getActivity(), new PageChangedObserver());
    }

    private void registerOnKeyListener() {
        search.setOnKeyListener(new SearchMediaListener());
    }

    private void registerOnClickListener() {

        sortButton.setOnClickListener((View view) -> mediaActivityViewModel.sortMediaByName());

        backButton.setOnClickListener((View view) -> mediaActivity.switchToMediaListStepOne());

        nextPageButton.setOnClickListener((View view) -> currentPage.postValue(currentPage.getValue() + 1));

        previousPageButton.setOnClickListener((View view) -> currentPage.postValue(currentPage.getValue() - 1));

        newMediaButton.setOnClickListener((View view) -> {

            Media media = new Media();

            mediaActivityViewModel.selectMedia(media);

            mediaActivity.switchToCreationStepOne();
        });
    }

    private class MediaInsertedObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            linearLayoutMedia.removeAllViews();

            for (Media media : mediaList) {

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
    }

    private class PageChangedObserver implements Observer<Integer> {

        @Override
        public void onChanged(Integer pageNumber) {

            int numberOfPages = 99;
            int numberOfDigits = String.valueOf(numberOfPages).length();

            String numberFormat = "%0" + numberOfDigits + "d";

            String formattedCurrentPageNumber = String.format(Locale.getDefault(), numberFormat, pageNumber);
            String formattedTotalPageNumbers = String.format(Locale.getDefault(), numberFormat, numberOfPages);

            String numberOfPagesText = formattedCurrentPageNumber + " / " + formattedTotalPageNumbers;

            pageNumberLabel.setText(numberOfPagesText);
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