package com.example.tombolator.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedListComponent;
import com.example.tombolator.commons.PaginatedListEntry;

public class MediaListFragment extends Fragment {

    private MediaActivity mediaActivity;
    private MediaActivityViewModel mediaActivityViewModel;

    private PaginatedListComponent<Media> paginatedMediaList;

    private ImageView backButton;
    private ImageView createMediaButton;

    private MediaListFragment() {
    }

    public static MediaListFragment newInstance() {
        return new MediaListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mediaActivity = (MediaActivity) getActivity();
        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View view = inflater.inflate(R.layout.media_list_fragment, container, false);

        paginatedMediaList = view.findViewById(R.id.paginated_media_list);

        backButton = view.findViewById(R.id.back_button);
        createMediaButton = view.findViewById(R.id.create_media_button);

        configurePaginatedTombolaList();
        registerOnClickListener();
        registerObserver();

        return view;
    }

    private void configurePaginatedTombolaList() {

        paginatedMediaList.setItemSortingStringConverter(Media::getName);

        paginatedMediaList.setItemToViewConverter(media -> {

            @SuppressWarnings("unchecked")
            PaginatedListEntry<Media> paginatedListEntry = (PaginatedListEntry<Media>) View.inflate(
                    getContext(), R.layout.paginated_list_entry, null);

            paginatedListEntry.setText(media.toLabel());
            paginatedListEntry.setId(media.getId().intValue());

            paginatedListEntry.setOnClickListener(view -> {
                mediaActivityViewModel.selectMedia(media);
                mediaActivity.switchToMediaDetailsView(this);
            });

            return paginatedListEntry;
        });
    }

    private void registerObserver() {

        mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData().observe(getViewLifecycleOwner(), mediaList -> {
            if (mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData() != null)
                paginatedMediaList.setItems(getViewLifecycleOwner(), mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData());
        });
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(view -> mediaActivity.finish());

        createMediaButton.setOnClickListener(view -> {
            mediaActivityViewModel.selectMedia(new Media());
            mediaActivity.switchToCreationFragment(this);
        });
    }
}