package com.example.tombolator.media;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaMainFragment extends Fragment {

    private MediaActivity mediaActivity;
    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private MediaActivityViewModel mediaActivityViewModel;

    private EditText search;

    private LinearLayout linearLayoutMedia;

    private Button backButton;
    private Button nextPageButton;
    private Button previousPageButton;
    private Button newMediaButton;

    private MediaMainFragment() {}

    public static MediaMainFragment newInstance() {
        return new MediaMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.media_main_fragment, container, false);

        search = layout.findViewById(R.id.edit_text_search);

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.button_back);
        nextPageButton = layout.findViewById(R.id.button_next_page);
        previousPageButton = layout.findViewById(R.id.button_previous_page);
        newMediaButton = layout.findViewById(R.id.button_new_media);

        registerObserver();
        registerOnKeyListener();
        registerOnClickListener();

        refreshViewModel();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getMediaOnCurrentPage()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaInsertedObserver());
    }

    private void registerOnKeyListener() {
        search.setOnKeyListener(new SearchMediaListener());
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaActivity.finish();
            }
        });

        nextPageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaActivityViewModel.nextPage();
            }
        });

        previousPageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaActivityViewModel.previousPage();
            }
        });

        newMediaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mediaActivity.switchToCreateMediaView();
            }
        });
    }

    public void refreshViewModel() {

        AsyncTask.execute(new Runnable() {

            @Override
            public void run() {

                TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                        .getApplicationContext());

                final MediaDao mediaDao = context.getTomboDb().mediaDao();
                List<Long> mediaIds = mediaDao.getAllIds();
                List<Media> mediaList = new ArrayList<>();

                for (long id : mediaIds) {
                    mediaList.add(mediaDao.getById(id));
                }

                mediaActivityViewModel.addMedia(mediaList);
                mediaActivityViewModel.toFirstPage();
            }
        });
    }

    private class MediaInsertedObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {

            linearLayoutMedia.removeAllViews();

            for (Media media : mediaList) {

                long id = media.getId();
                String type = media.getType();

                TextView textView = (TextView) View.inflate(
                        mediaActivity.getApplicationContext(), R.layout.list_element, null);

                textView.setText(" " + media.toLabel());
                textView.setOnClickListener(showDetailsListener);
                textView.setId((int) id);

                switch(type) {

                    case Media.Type.CASSETTE:
                        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_cassette_25, 0, 0, 0);
                        break;

                    case Media.Type.CD:
                    case Media.Type.DVD:
                    case Media.Type.BLU_RAY:
                        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_cd_25, 0, 0, 0);
                        break;

                    case Media.Type.BOOK:
                        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_book_25, 0, 0, 0);
                        break;

                    case Media.Type.E_BOOK:
                        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                R.drawable.ic_ebook_25, 0, 0, 0);
                        break;

                    default: //No icon added
                }

                linearLayoutMedia.addView(textView);
            }
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