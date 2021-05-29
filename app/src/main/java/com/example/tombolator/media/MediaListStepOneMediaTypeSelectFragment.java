package com.example.tombolator.media;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;

import java.util.*;

public class MediaListStepOneMediaTypeSelectFragment extends Fragment {

    private static final int UNSELECTED = 0;
    private static final int SELECTED = 1;

    private MediaActivity mediaActivity;
    private final View.OnClickListener toggleMediaTypeSelectListener = new ToggleMediaTypeSelectListener();

    private MediaActivityViewModel mediaActivityViewModel;

    private LinearLayout mediaTypeLinearLayout;

    private Button backButton;
    private Button continueButton;

    private MediaListStepOneMediaTypeSelectFragment() {}

    public static MediaListStepOneMediaTypeSelectFragment newInstance() {
        return new MediaListStepOneMediaTypeSelectFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_list_step_one_type_select, container, false);

        mediaTypeLinearLayout = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.button_back);
        continueButton = layout.findViewById(R.id.button_continue);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {

        mediaActivityViewModel.getAllMediaLiveData()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaTypesUpdatedObserver());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener((View view) -> mediaActivity.finish());

        continueButton.setOnClickListener((View view) -> {

            if(mediaActivityViewModel.getSelectedMediaTypes().getValue() == null) {
                /* TODO: Add log entry. */
                throw new NullPointerException();
            }

            List<String> mediaTypes = new ArrayList<>();

            for(int i=0; i<mediaTypeLinearLayout.getChildCount(); i++) {

                TextView mediaTypeTextView = (TextView) mediaTypeLinearLayout.getChildAt(i);

                if(mediaTypeTextView.getId() == SELECTED)
                    mediaTypes.add(mediaTypeTextView.getText().toString());
            }

            mediaActivityViewModel.selectMediaTypes(mediaTypes);

            mediaActivity.switchToMediaListStepTwo();
        });
    }

    private class MediaTypesUpdatedObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            Set<String> mediaTypes = new HashSet<>();
            mediaList.forEach((Media media) -> mediaTypes.add(media.getMediaType()));

            mediaTypeLinearLayout.removeAllViews();

            for(String mediaType : mediaTypes) {

                /* TODO: Replace with icon views. */
                TextView mediaTypeTextView = (TextView) View.inflate(
                        mediaActivity.getApplicationContext(), R.layout.list_element, null);

                mediaTypeTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        MediaUtil.getMediaTypeIcon(mediaType), 0, 0, 0);

                mediaTypeTextView.setText(mediaType);
                mediaTypeTextView.setId(UNSELECTED);
                mediaTypeTextView.setOnClickListener(toggleMediaTypeSelectListener);
                mediaTypeTextView.setTextSize(20);

                mediaTypeLinearLayout.addView(mediaTypeTextView);
            }
        }
    }

    private static class ToggleMediaTypeSelectListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;

            switch(textView.getId()) {

                case UNSELECTED:
                    textView.setTextColor(Color.CYAN);
                    textView.setId(SELECTED);
                    break;

                case SELECTED:
                    textView.setTextColor(Color.BLACK);
                    textView.setId(UNSELECTED);
                    break;

                default:
                    /* TODO: Add error log. */
            }
        }
    }
}