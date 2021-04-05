package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MediaActivityViewModel extends ViewModel {

    private static final int MEDIA_PER_PAGE = 8;
    private int currentPage = 1;

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<Media>> mediaOnCurrentPage = new MutableLiveData<>(new ArrayList<Media>());
    private final MutableLiveData<ArrayList<Media>> mediaDatabaseAsList = new MutableLiveData<>(new ArrayList<Media>());
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

        int start = pageNumber * MEDIA_PER_PAGE - MEDIA_PER_PAGE;
        int end = start + MEDIA_PER_PAGE;

        /* Shortens index for end if last page is < MEDIA_PER_PAGE */
        if(pageNumber == getNumberOfPages()) {
            end = start + mediaDatabaseAsList.getValue().size() % MEDIA_PER_PAGE;
        }

        mediaOnCurrentPage.getValue().clear();

        for(int i=start; i<end; i++) {

            Media media = mediaDatabaseAsList.getValue().get(i);
            mediaOnCurrentPage.getValue().add(media);
        }

        mediaOnCurrentPage.postValue(mediaOnCurrentPage.getValue());
    }

    private int getNumberOfPages() {
        return mediaDatabaseAsList.getValue().size() / MEDIA_PER_PAGE;
    }

    public void addMedia(List<Media> mediaList) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseLiveData.getValue().clear();

        for(Media media : mediaList) {

            mediaDatabaseAsList.getValue().add(media);
            mediaDatabaseLiveData.getValue().put(media.getId(), media);
        }

        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
    }

    public void removeMedia(long mediaId) {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseAsList.getValue().remove(mediaDatabaseLiveData.getValue().get(mediaId));
        mediaDatabaseLiveData.getValue().remove(mediaId);

        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
    }

    public void removeAllMedia() {

        if(mediaDatabaseLiveData.getValue() == null)
            throw new NullPointerException();

        mediaDatabaseAsList.getValue().clear();
        mediaDatabaseLiveData.getValue().clear();
        
        mediaDatabaseLiveData.postValue(mediaDatabaseLiveData.getValue());
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