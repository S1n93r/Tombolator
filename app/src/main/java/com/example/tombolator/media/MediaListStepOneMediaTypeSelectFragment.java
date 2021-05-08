package com.example.tombolator.media;

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

import java.util.List;
import java.util.Objects;

public class MediaListStepOneMediaTypeSelectFragment extends Fragment {

    private MediaActivity mediaActivity;
    private final View.OnClickListener selecteOrUnselectMediaType = new SelecteOrUnselectMediaType();

    private MediaActivityViewModel mediaActivityViewModel;

    private LinearLayout linearLayoutMedia;

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

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.button_back);
        continueButton = layout.findViewById(R.id.button_continue);

        registerObserver();
        registerOnClickListener();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getAvailableMediaTypes()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaTypesUpdated());
    }

    private void registerOnClickListener() {
        backButton.setOnClickListener(view -> {
            mediaActivity.finish();
        });
        continueButton.setOnClickListener(view -> {

            /*
            if(mediaActivityViewModel.getSelectedMediaTypes().getValue().isEmpty())
                return;

             */

            mediaActivity.switchToMediaListStepTwo();
        });
    }

    private class MediaTypesUpdated implements Observer<List<String>> {

        @Override
        public void onChanged(List<String> mediaTypeList) {

            linearLayoutMedia.removeAllViews();

            for (String mediaType : mediaTypeList) {

                TextView textView = (TextView) View.inflate(
                        mediaActivity.getApplicationContext(), R.layout.list_element, null);

                textView.setText(mediaType);
                textView.setOnClickListener(selecteOrUnselectMediaType);

                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        MediaUtil.getMediaIcon(mediaType), 0, 0, 0);

                linearLayoutMedia.addView(textView);
            }
        }
    }

    private class SelecteOrUnselectMediaType implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            /* TODO: Implement media type selection. */
        }
    }
}