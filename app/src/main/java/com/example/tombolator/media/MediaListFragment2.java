package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedMediaList;
import com.example.tombolator.tombolas.TombolasActivityViewModel;

public class MediaListFragment2 extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;
    private TombolasActivityViewModel tombolasActivityViewModel;

    private PaginatedMediaList paginatedMediaList;

    private MediaListFragment2() {}

    public static MediaListFragment2 newInstance() {
        return new MediaListFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View view = inflater.inflate(R.layout.media_list_fragment_2, container, false);

        paginatedMediaList = view.findViewById(R.id.paginated_media_list);

        paginatedMediaList.configureView(this, mediaActivityViewModel.getAllMediaLiveData(),
                v -> mediaActivity.finish());

        return view;
    }
}