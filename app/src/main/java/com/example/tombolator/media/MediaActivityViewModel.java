package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.*;

public class MediaActivityViewModel extends ViewModel {

    private static final String DEFAULT_SEARCH_FILTER = "[show all media]";
    private static final int MEDIA_PER_PAGE = 8;

    private static final int SORTING_REGULAR = 0;
    private static final int SORTING_REVERSED = 1;

    private int currentSortingMode = SORTING_REGULAR;

    private String currentSearchFilter = DEFAULT_SEARCH_FILTER;

    private int currentPage = 1;

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Media>> mediaOnCurrentPage = new MutableLiveData<>(new ArrayList<>());
    private final ArrayList<Media> mediaListFiltered = new ArrayList<>();

    private final MutableLiveData<ArrayList<Media>> mediaList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<HashMap<Long, Media>> mediaDatabaseLiveData = new MutableLiveData<>(new HashMap<>());

    public void toFirstPage() {
        currentPage = 1;
        setMediaOnPageListToPage(currentPage);
    }

    public void nextPage() {

        if(currentPage < getNumberOfPages()) {
            currentPage++;
            setMediaOnPageListToPage(currentPage);
        }
    }

    public void previousPage() {

        if(currentPage > 1) {
            currentPage--;
            setMediaOnPageListToPage(currentPage);
        }
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

    private int getNumberOfPages() {
        return mediaListFiltered.size() / MEDIA_PER_PAGE;
    }

    public void  clearAndAddMedia(List<Media> mediaList) {

        Objects.requireNonNull(this.mediaList.getValue()).clear();
        Objects.requireNonNull(mediaDatabaseLiveData.getValue()).clear();

        for(Media media : mediaList) {

            if(this.mediaList.getValue() == null) {
                /* TODO: Add error log here */
                return;
            }

            this.mediaList.getValue().add(media);
            mediaDatabaseLiveData.getValue().put(media.getId(), media);
        }

        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
        applySearchFilter();
    }

    public void removeMedia(long mediaId) {

        if (this.mediaList.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        if (mediaDatabaseLiveData.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        mediaList.getValue().remove(mediaDatabaseLiveData.getValue().get(mediaId));
        mediaDatabaseLiveData.getValue().remove(mediaId);

        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
        applySearchFilter();
    }

    public void selectMedia(long mediaId) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        Media media = mediaDatabaseLiveData.getValue().get(mediaId);

        if(media == null) {
            System.err.println("Medium not found. Selected media remains null.");
            return;
        }

        selectedMedia.postValue(media);
    }

    public void setMediaSearchFilter(String searchFilter) {

        if(searchFilter.isEmpty()) {
            clearMediaSearchFilter();
            return;
        }

        currentSearchFilter = searchFilter;
        applySearchFilter();
    }

    public void clearMediaSearchFilter() {
        currentSearchFilter = DEFAULT_SEARCH_FILTER;
        applySearchFilter();
    }

    private void applySearchFilter() {

        if (mediaList.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        mediaListFiltered.clear();

        Collection<Media> filteredCollection = Collections2.filter(
                mediaList.getValue(), new MediaPredicate(currentSearchFilter));
        mediaListFiltered.addAll(filteredCollection);

        toFirstPage();
    }

    private static class MediaPredicate implements Predicate<Media> {

        private final String searchFilter;

        public MediaPredicate(String searchFilter) {
            this.searchFilter = searchFilter;
        }

        @Override
        public boolean apply(@NullableDecl Media media) {

            if(searchFilter.equals(DEFAULT_SEARCH_FILTER))
                return true;

            if (media == null) {
                /* TODO: Add error log here */
                return false;
            }

            return media.toLabel().contains(searchFilter);
        }
    }

    public void sortMediaByName() {

        if(currentSortingMode == SORTING_REVERSED) {
            mediaListFiltered.sort(new MediaComparator().reversed());
            currentSortingMode = SORTING_REGULAR;
        }else {
            mediaListFiltered.sort(new MediaComparator());
            currentSortingMode = SORTING_REVERSED;
        }

        toFirstPage();
    }

    private static class MediaComparator implements Comparator<Media> {

        @Override
        public int compare(Media m1, Media m2) {

            String titleAndName1 = m1.getTitle() + m1.getName();
            String titleAndName2 = m2.getTitle() + m2.getName();

            return titleAndName1.compareTo(titleAndName2);
        }
    }

    public LiveData<HashMap<Long, Media>> getMediaDatabase() {
        return mediaDatabaseLiveData;
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }

    public MutableLiveData<ArrayList<Media>> getMediaOnCurrentPage() {
        return mediaOnCurrentPage;
    }
}