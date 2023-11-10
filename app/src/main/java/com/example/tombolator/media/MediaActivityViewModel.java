package com.example.tombolator.media;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.tombolator.TomboRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MediaActivityViewModel extends AndroidViewModel {

    private static final String FILTER_ALL_CATEGORIES = "[show all categories]";

    private static final int SORTING_NONE = 0;
    private static final int SORTING_REGULAR = 1;
    private static final int SORTING_REVERSED = 2;

    private final TomboRepository tomboRepository;
    private final LiveData<List<Media>> allMediaLiveData;
    private final MutableLiveData<List<Media>> allMediaFilteredAndSortedLiveData = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<String> selectedMediaType = new MutableLiveData<>("");

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    private int currentSortingMode = SORTING_NONE;

    public MediaActivityViewModel(@NonNull Application application) {

        super(application);

        tomboRepository = new TomboRepository(application);
        allMediaLiveData = tomboRepository.getAllMediaLiveData();

        registerObserver();
    }

    private void registerObserver() {
        allMediaLiveData.observeForever(new AllMediaSortAndFilterObserver());
    }

    public void selectMediaType(String mediaType) {

        if (selectedMediaType.getValue() == null) {
            /* Add logger entry.  */
            throw new NullPointerException();
        }

        /* TODO: Move to background thread? */
        selectedMediaType.setValue(mediaType);

        refreshFilteredAndSortedMediaLiveData();
    }

    public void clearMediaType() {

        selectedMediaType.setValue(FILTER_ALL_CATEGORIES);

        refreshFilteredAndSortedMediaLiveData();
    }

    public Media getMedia(long mediaId) {

        if (allMediaLiveData.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        for (Media media : allMediaLiveData.getValue()) {

            if (media.getId() == mediaId)
                return media;
        }

        return null;
    }

    public void selectMedia(Media media) {
        selectedMedia.setValue(Objects.requireNonNull(media));
        selectedMedia.postValue(selectedMedia.getValue());
    }

    public void selectMedia(long mediaId) {

        if (allMediaLiveData.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        for (Media media : allMediaLiveData.getValue()) {

            if (media.getId() == mediaId) {

                selectedMedia.postValue(media);
                return;
            }
        }

        System.err.println("Media with id " + mediaId + " was not found in " + this.getClass() + ".");
    }

    public void toggleSorting() {

        switch (currentSortingMode) {

            case SORTING_NONE:
                currentSortingMode = SORTING_REGULAR;
                break;
            case SORTING_REGULAR:
                currentSortingMode = SORTING_REVERSED;
                break;
            case SORTING_REVERSED:
            default:
                currentSortingMode = SORTING_NONE;
        }

        applySorting();

        allMediaFilteredAndSortedLiveData.postValue(allMediaFilteredAndSortedLiveData.getValue());
    }

    private void refreshFilteredAndSortedMediaLiveData() {

        applyMediaTypeFilterAndPopulate();
        applySorting();

        allMediaFilteredAndSortedLiveData.postValue(allMediaFilteredAndSortedLiveData.getValue());
    }

    public void applyMediaTypeFilterAndPopulate() {

        if (allMediaLiveData.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        if (allMediaFilteredAndSortedLiveData.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        allMediaFilteredAndSortedLiveData.getValue().clear();

        Collection<Media> filteredCollection = Collections2.filter(
                allMediaLiveData.getValue(), new MediaTypeFilterPredicate(selectedMediaType.getValue()));

        allMediaFilteredAndSortedLiveData.getValue().addAll(filteredCollection);
    }

    private void applySorting() {

        if (allMediaFilteredAndSortedLiveData.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        allMediaFilteredAndSortedLiveData.getValue().sort(new MediaComparator());

        switch (currentSortingMode) {

            case SORTING_REGULAR:
                allMediaFilteredAndSortedLiveData.getValue().sort(new MediaComparator());
                break;

            case SORTING_REVERSED:
                allMediaFilteredAndSortedLiveData.getValue().sort(new MediaComparator().reversed());
                break;

            case SORTING_NONE:
            default: /* TODO: Implement default sorting. */
        }
    }

    public void insert(Media media) {
        tomboRepository.insertMedia(media);
    }

    public void insertAll(List<Media> mediaList) {
        tomboRepository.insertAllMedia(mediaList);
    }

    public void update(Media media) {
        tomboRepository.updateMedia(media);
    }

    public void updateAll(List<Media> mediaList) {
        tomboRepository.updateAllMedia(mediaList);
    }

    public void delete(Media media) {
        tomboRepository.deleteMedia(media);
    }

    public void deleteAllMedia() {
        tomboRepository.deleteAllMedia();
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }

    public LiveData<List<Media>> getAllMediaLiveData() {
        return allMediaLiveData;
    }

    public LiveData<List<Media>> getAllMediaFilteredAndSortedLiveData() {
        return allMediaFilteredAndSortedLiveData;
    }

    public MutableLiveData<String> getSelectedMediaType() {
        return selectedMediaType;
    }

    private static class MediaTypeFilterPredicate implements Predicate<Media> {

        private final String mediaType;

        public MediaTypeFilterPredicate(String mediaType) {
            this.mediaType = mediaType;
        }

        @Override
        public boolean apply(@NullableDecl Media media) {

            if (media == null) {
                /* TODO: Add error log here */
                throw new NullPointerException();
            }

            if (mediaType.equals(FILTER_ALL_CATEGORIES))
                return true;

            return mediaType.equals(media.getMediaType());
        }
    }

    private static class MediaComparator implements Comparator<Media> {

        @Override
        public int compare(Media m1, Media m2) {

            String titleAndName1 = m1.getName() + m1.getTitle();
            String titleAndName2 = m2.getName() + m2.getTitle();

            return titleAndName1.compareTo(titleAndName2);
        }
    }

    private class AllMediaSortAndFilterObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {
            refreshFilteredAndSortedMediaLiveData();
        }
    }
}