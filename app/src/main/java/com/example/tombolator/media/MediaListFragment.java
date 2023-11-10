package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedMediaList;

public class MediaListFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private MediaListFragment() {}

    public static MediaListFragment newInstance() {
        return new MediaListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View view = inflater.inflate(R.layout.media_list_fragment, container, false);

        PaginatedMediaList paginatedMediaList = view.findViewById(R.id.paginated_media_list);

        paginatedMediaList.configureView(this, mediaActivityViewModel.getAllMediaLiveData(),
                v -> mediaActivity.finish(), new EnterCreationListener(this));

        paginatedMediaList.getSelectedMedia().observe(getViewLifecycleOwner(), new ShowDetailsObserver(this));

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

    private class ShowDetailsObserver implements Observer<Media> {

        private final Fragment fragmentBefore;

        public ShowDetailsObserver(Fragment fragmentBefore) {
            this.fragmentBefore = fragmentBefore;
        }

        @Override
        public void onChanged(Media media) {

            mediaActivityViewModel.selectMedia(media);

            mediaActivity.switchToMediaDetailsView(fragmentBefore);
        }
    }
}