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

import java.util.*;

public class MediaActivityViewModel extends AndroidViewModel {

    private static final String FILTER_NONE = "[show all media]";
    private static final int MEDIA_PER_PAGE = 8;

    private static final int SORTING_NONE = 0;
    private static final int SORTING_REGULAR = 1;
    private static final int SORTING_REVERSED = 2;

    private final TomboRepository tomboRepository;
    private final LiveData<List<Media>> allMediaLiveData;
    private final MutableLiveData<List<Media>> allMediaFilteredAndSortedLiveData = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<String>> availableMediaTypes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<ArrayList<String>> selectedMediaTypes = new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<ArrayList<Media>> mediaOnCurrentPage = new MutableLiveData<>(new ArrayList<>());
    private final ArrayList<Media> mediaListFiltered = new ArrayList<>();

    private int currentSortingMode = SORTING_NONE;

    private String currentSearchFilter = FILTER_NONE;

    public MediaActivityViewModel(@NonNull Application application) {

        super(application);

        tomboRepository = new TomboRepository(application);
        allMediaLiveData = tomboRepository.getAllMediaLiveData();
        applySearchFilterAndPopulate(allMediaFilteredAndSortedLiveData);

        registerObserver();
    }

    private void registerObserver() {

        allMediaLiveData.observeForever(new AllMediaSortAndFilterObserver());
    }

    public Media getMedia(long mediaId) {

        for(Media media : allMediaLiveData.getValue()) {

            if(media.getId() == mediaId)
                return media;
        }

        return null;
    }

    private void setMediaOnPageListToPage(int pageNumber) {

        if(mediaOnCurrentPage.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        int start = (pageNumber - 1) * MEDIA_PER_PAGE;
        int end = start + MEDIA_PER_PAGE;

        if(end > mediaListFiltered.size())
            end = mediaListFiltered.size();

        mediaOnCurrentPage.getValue().clear();

        for(int i=start; i<end; i++) {

            Media media = mediaListFiltered.get(i);
            mediaOnCurrentPage.getValue().add(media);
        }

        mediaOnCurrentPage.postValue(mediaOnCurrentPage.getValue());
    }

    public int getNumberOfPages() {
        return mediaListFiltered.size() / MEDIA_PER_PAGE;
    }

    public void selectMedia(Media media) {
        selectedMedia.setValue(Objects.requireNonNull(media));
        selectedMedia.postValue(selectedMedia.getValue());
    }

    public void selectMedia(long mediaId) {

        for(Media media : allMediaLiveData.getValue()) {

            if(media.getId() == mediaId) {

                selectedMedia.postValue(media);
                return;
            }
        }

        System.err.println("Media with id " + mediaId + " was not found in " + this.getClass() + ".");
    }

    public void setMediaSearchFilter(String searchFilter) {

        if(searchFilter.isEmpty()) {
            clearMediaSearchFilter();
            return;
        }

        currentSearchFilter = searchFilter;
        refreshFilteredAndSortedMediaLiveData();
    }

    public void clearMediaSearchFilter() {
        currentSearchFilter = FILTER_NONE;
        refreshFilteredAndSortedMediaLiveData();
    }

    public void toggleSorting() {

        switch(currentSortingMode) {

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

        refreshFilteredAndSortedMediaLiveData();
    }

    private void refreshFilteredAndSortedMediaLiveData() {

        applySearchFilterAndPopulate(allMediaFilteredAndSortedLiveData);
        applySorting(allMediaFilteredAndSortedLiveData);

        allMediaFilteredAndSortedLiveData.postValue(allMediaFilteredAndSortedLiveData.getValue());
    }

    private void applySearchFilterAndPopulate(LiveData<List<Media>> mediaListLiveData) {

        if(allMediaLiveData.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        if(mediaListLiveData.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        mediaListLiveData.getValue().clear();

        if(currentSearchFilter.equals(FILTER_NONE)) {

            mediaListLiveData.getValue().addAll(allMediaLiveData.getValue());
            return;
        }

        Collection<Media> filteredCollection = Collections2.filter(
                allMediaLiveData.getValue(), new ApplySearchFilterToMediaListPredicate(currentSearchFilter));

        mediaListLiveData.getValue().clear();
        mediaListLiveData.getValue().addAll(filteredCollection);
    }

    private static class ApplySearchFilterToMediaListPredicate implements Predicate<Media> {

        private final String searchFilter;

        public ApplySearchFilterToMediaListPredicate(String searchFilter) {
            this.searchFilter = searchFilter;
        }

        @Override
        public boolean apply(@NullableDecl Media media) {

            if(searchFilter.equals(FILTER_NONE))
                return true;

            if (media == null) {
                /* TODO: Add error log here */
                return false;
            }

            return media.toLabel().contains(searchFilter);
        }
    }

    private void applySorting(LiveData<List<Media>> mediaListLiveData) {

        if(mediaListLiveData.getValue() == null) {
            /* TODO: Add log entry. */
            return;
        }

        switch(currentSortingMode) {

            case SORTING_REGULAR:
                mediaListLiveData.getValue().sort(new MediaComparator());
                break;

            case SORTING_REVERSED:
                mediaListLiveData.getValue().sort(new MediaComparator().reversed());
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

    private static class MediaComparator implements Comparator<Media> {

        @Override
        public int compare(Media m1, Media m2) {

            String titleAndName1 = m1.getName() + m1.getTitle();
            String titleAndName2 = m2.getName() + m2.getTitle();

            return titleAndName1.compareTo(titleAndName2);
        }
    }

    private synchronized void updateMediaTypes() {

        Set<String> set = new HashSet<>();

        for(Media media : allMediaLiveData.getValue()) {
            set.add(media.getType());
        }

        for(String mediaType : set) {
            availableMediaTypes.getValue().add(mediaType);
        }

        availableMediaTypes.postValue(availableMediaTypes.getValue());
    }

    private class AllMediaSortAndFilterObserver implements Observer<List<Media>> {

        @Override
        public void onChanged(List<Media> mediaList) {
            refreshFilteredAndSortedMediaLiveData();
        }
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }

    public MutableLiveData<ArrayList<String>> getAvailableMediaTypes() {
        return availableMediaTypes;
    }

    public LiveData<List<Media>> getAllMediaLiveData() {
        return allMediaLiveData;
    }

    public MutableLiveData<List<Media>> getAllMediaFilteredAndSortedLiveData() {
        return allMediaFilteredAndSortedLiveData;
    }
}