package com.example.tombolator.media;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MediaMainFragment extends Fragment {

    private MediaActivity mediaActivity;
    private final View.OnClickListener showDetailsListener = new ShowDetailsListener();

    private MediaActivityViewModel mediaActivityViewModel;

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

        linearLayoutMedia = layout.findViewById(R.id.linear_layout_media);

        backButton = layout.findViewById(R.id.button_back);
        nextPageButton = layout.findViewById(R.id.button_next_page);
        previousPageButton = layout.findViewById(R.id.button_previous_page);
        newMediaButton = layout.findViewById(R.id.button_new_media);

        registerObserver();
        registerOnClickListener();
        registerOnTouchListener();

        refreshViewModel();

        return layout;
    }

    private void registerObserver() {
        mediaActivityViewModel.getMediaOnCurrentPage()
                .observe(Objects.requireNonNull(this.getActivity()), new MediaInsertedToListObserver());
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

    private void registerOnTouchListener() {

        linearLayoutMedia.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            public void onSwipeRight() {
                mediaActivityViewModel.previousPage();
            }

            public void onSwipeLeft() {
                mediaActivityViewModel.nextPage();
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

    private class MediaInsertedToListObserver implements Observer<List<Media>> {

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

    private class MediaInsertedObserver implements Observer<Map<Long, Media>> {

        @Override
        public void onChanged(Map<Long, Media> mediaMapInserted) {

            linearLayoutMedia.removeAllViews();

            for (Map.Entry<Long, Media> pair : mediaMapInserted.entrySet()) {

                Media media = pair.getValue();

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

    private class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        private OnSwipeTouchListener(Context context) {
            this.gestureDetector = new GestureDetector(context, new GestureListener());
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }
    }
}