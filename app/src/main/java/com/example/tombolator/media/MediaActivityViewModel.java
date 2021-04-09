package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MediaActivityViewModel extends ViewModel {

    private static final String DEFAULT_SEARCH_FILTER = "[show all media]";
    private static final int MEDIA_PER_PAGE = 8;

    private String currentSearchFilter = DEFAULT_SEARCH_FILTER;

    private int currentPage = 1;

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Media>> mediaOnCurrentPage = new MutableLiveData<>(new ArrayList<Media>());
    private final MutableLiveData<ArrayList<Media>> mediaListFiltered = new MutableLiveData<>(new ArrayList<Media>());

    private final MutableLiveData<ArrayList<Media>> mediaList = new MutableLiveData<>(new ArrayList<Media>());
    private final MutableLiveData<HashMap<Long, Media>> mediaDatabaseLiveData = new MutableLiveData<>(new HashMap<Long, Media>());

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

        if(mediaListFiltered.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        if(mediaOnCurrentPage.getValue() == null) {
            /* TODO: Add error log here */
            return;
        }

        int start = pageNumber * MEDIA_PER_PAGE - MEDIA_PER_PAGE;
        int end = start + MEDIA_PER_PAGE;

        /* Shortens index for end if last page is < MEDIA_PER_PAGE */
        if(pageNumber == getNumberOfPages()) {
            end = start + mediaListFiltered.getValue().size() % MEDIA_PER_PAGE;
        }

        mediaOnCurrentPage.getValue().clear();

        for(int i=start; i<end; i++) {

            Media media = mediaListFiltered.getValue().get(i);
            mediaOnCurrentPage.getValue().add(media);
        }

        mediaOnCurrentPage.postValue(mediaOnCurrentPage.getValue());
    }

    private int getNumberOfPages() {

        if(mediaList.getValue() == null) {
            /* TODO: Add error log here */
            return 0;
        }

        return mediaList.getValue().size() / MEDIA_PER_PAGE;
    }

    public void addMedia(List<Media> mediaList) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseLiveData.getValue().clear();

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

        if(currentSearchFilter.equals(DEFAULT_SEARCH_FILTER)) {

            if (mediaListFiltered.getValue() == null) {
                /* TODO: Add error log here */
                return;
            }

            if(mediaListFiltered.getValue().size() == mediaList.getValue().size())
                return;

            mediaListFiltered.getValue().clear();
            mediaListFiltered.getValue().addAll(mediaList.getValue());
            mediaListFiltered.postValue(mediaList.getValue());
            return;
        }

        Collection<Media> filteredCollection = Collections2.filter(mediaList.getValue(), new MediaPredicate(currentSearchFilter));
        mediaListFiltered.postValue(new ArrayList<>(filteredCollection));
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