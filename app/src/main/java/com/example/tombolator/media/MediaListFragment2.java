package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedMediaList;

public class MediaListFragment2 extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private PaginatedMediaList paginatedMediaList;

    private MediaListFragment2() {}

    public static MediaListFragment2 newInstance() {
        return new MediaListFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View view = inflater.inflate(R.layout.media_list_fragment_2, container, false);

        paginatedMediaList = view.findViewById(R.id.paginated_media_list);

        paginatedMediaList.configureView(this, mediaActivityViewModel.getAllMediaLiveData(),
                v -> mediaActivity.finish(), new EnterCreationListener(this), new ShowDetailsListener(this));

        return view;
    }

    private class EnterCreationListener implements View.OnClickListener {

        private final Fragment fragmentBefore;

        public EnterCreationListener(Fragment fragmentBefore) {
            this.fragmentBefore = fragmentBefore;
        }

        @Override
        public void onClick(View v) {

            Media createdMedia = new Media();

            createdMedia.setCreationTimestamp(System.currentTimeMillis());

            mediaActivityViewModel.selectMedia(createdMedia);

            mediaActivity.switchToCreationStepOne(fragmentBefore);
        }
    }

    private class ShowDetailsListener implements View.OnClickListener {

        private final Fragment fragmentBefore;

        public ShowDetailsListener(Fragment fragmentBefore) {
            this.fragmentBefore = fragmentBefore;
        }

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long mediaId = textView.getId();
            mediaActivityViewModel.selectMedia(mediaId);

            mediaActivity.switchToMediaDetailsView(fragmentBefore);
        }
    }
}