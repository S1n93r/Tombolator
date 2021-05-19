package com.example.tombolator;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaDao;
import com.example.tombolator.tombolas.Tombola;
import com.example.tombolator.tombolas.TombolaDao;

import java.util.List;

public class TomboRepository {

    private MediaDao mediaDao;
    private TombolaDao tombolaDao;

    private LiveData<List<Media>> allMediaLiveData;
    private LiveData<List<Tombola>> allTombolasLiveData;

    public TomboRepository(Application application) {

        TomboDatabase tomboDatabase = TomboDatabase.getInstance(application);

        mediaDao = tomboDatabase.mediaDao();
        tombolaDao = tomboDatabase.tombolaDao();

        allMediaLiveData = mediaDao.getAllMediaAsLiveData();
        allTombolasLiveData = tombolaDao.getAllTombolasAsLiveData();
    }

    public void insertMedia(Media media) {
        new InsertMediaAsyncTask(mediaDao).execute(media);
    }

    public void insertAllMedia(List<Media> mediaList) {
        new InsertAllMediaAsyncTask(mediaDao).execute(mediaList);
    }

    public void updateMedia(Media media) {
        new UpdateMediaAsyncTask(mediaDao).execute(media);
    }

    public void updateAllMedia(List<Media> mediaList) {
        new UpdateAllMediaAsyncTask(mediaDao).execute(mediaList);
    }

    public void deleteMedia(Media media) {
        new DeleteMediaAsyncTask(mediaDao).execute(media);
    }

    public void deleteAllMedia() {
        new DeleteAllMediaAsyncTask(mediaDao).execute();
    }

    public void insertTombola(Tombola tombola) {
        new InsertTombolaAsyncTask(tombolaDao).execute(tombola);
    }

    public void insertAllTombolas(List<Tombola> tombolaList) {
        new InsertAllTombolasAsyncTask(tombolaDao).execute(tombolaList);
    }

    public void updateTombola(Tombola tombola) {
        new UpdateTombolaAsyncTask(tombolaDao).execute(tombola);
    }

    public void updateAllTombolas(List<Tombola> tombolaList) {
        new UpdateAllTombolasAsyncTask(tombolaDao).execute(tombolaList);
    }

    public void deleteTombola(Tombola tombola) {
        new DeleteTombolaAsyncTask(tombolaDao).execute(tombola);
    }

    public void deleteAllTombolas() {
        new DeleteAllTombolaAsyncTask(tombolaDao).execute();
    }

    public void deleteMediaFromAllTombolas(Media media) {
        new DeleteMediaFromAllTombolasAsyncTask(tombolaDao).execute(media);
    }

    public LiveData<List<Media>> getAllMediaLiveData() {
        return allMediaLiveData;
    }

    public LiveData<List<Tombola>> getAllTombolasLiveData() {
        return allTombolasLiveData;
    }

    private static class InsertMediaAsyncTask extends AsyncTask<Media, Void, Void> {

        private final MediaDao mediaDao;

        public InsertMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected Void doInBackground(Media... media) {

            mediaDao.insertMedia(media[0]);
            return null;
        }
    }

    private static class InsertAllMediaAsyncTask extends AsyncTask<List<Media>, Void, Void> {

        private final MediaDao mediaDao;

        public InsertAllMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected Void doInBackground(List<Media>... lists) {

            mediaDao.insertAllMedia(lists[0]);
            return null;
        }
    }

    private static class UpdateMediaAsyncTask extends AsyncTask<Media, Void, Void> {

        private final MediaDao mediaDao;

        public UpdateMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected Void doInBackground(Media... media) {

            mediaDao.updateMedia(media[0]);
            return null;
        }
    }

    private static class UpdateAllMediaAsyncTask extends AsyncTask<List<Media>, Void, Void> {

        private final MediaDao mediaDao;

        public UpdateAllMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected Void doInBackground(List<Media>... lists) {

            mediaDao.updateAllMedia(lists[0]);
            return null;
        }
    }

    private static class DeleteMediaAsyncTask extends AsyncTask<Media, Void, Void> {

        private final MediaDao mediaDao;

        public DeleteMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }

        @Override
        protected Void doInBackground(Media... media) {

            mediaDao.deleteMedia(media[0]);
            return null;
        }
    }

    private static class DeleteAllMediaAsyncTask extends AsyncTask<Void, Void, Void> {

        private final MediaDao mediaDao;

        public DeleteAllMediaAsyncTask(MediaDao mediaDao) {
            this.mediaDao = mediaDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            mediaDao.nukeTable();
            return null;
        }
    }

    private static class InsertTombolaAsyncTask extends AsyncTask<Tombola, Void, Void> {

        private final TombolaDao tombolaDao;

        public InsertTombolaAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(Tombola... tombolas) {

            tombolaDao.insertTombola(tombolas[0]);
            return null;
        }
    }

    private static class InsertAllTombolasAsyncTask extends AsyncTask<List<Tombola>, Void, Void> {

        private final TombolaDao tombolaDao;

        public InsertAllTombolasAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(List<Tombola>... lists) {

            tombolaDao.insertAllTombolas(lists[0]);
            return null;
        }
    }

    private static class UpdateTombolaAsyncTask extends AsyncTask<Tombola, Void, Void> {

        private final TombolaDao tombolaDao;

        public UpdateTombolaAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(Tombola... tombolas) {

            tombolaDao.updateTombola(tombolas[0]);
            return null;
        }
    }

    private static class UpdateAllTombolasAsyncTask extends AsyncTask<List<Tombola>, Void, Void> {

        private final TombolaDao tombolaDao;

        public UpdateAllTombolasAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(List<Tombola>... lists) {

            tombolaDao.updateAllTombolas(lists[0]);
            return null;
        }
    }

    private static class DeleteTombolaAsyncTask extends AsyncTask<Tombola, Void, Void> {

        private final TombolaDao tombolaDao;

        public DeleteTombolaAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(Tombola... tombolas) {

            tombolaDao.deleteMedia(tombolas[0]);
            return null;
        }
    }

    private static class DeleteAllTombolaAsyncTask extends AsyncTask<Void, Void, Void> {

        private final TombolaDao tombolaDao;

        public DeleteAllTombolaAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            tombolaDao.nukeTable();
            return null;
        }
    }

    private static class DeleteMediaFromAllTombolasAsyncTask extends AsyncTask<Media, Void, Void> {

        private final TombolaDao tombolaDao;

        public DeleteMediaFromAllTombolasAsyncTask(TombolaDao tombolaDao) {
            this.tombolaDao = tombolaDao;
        }

        @Override
        protected Void doInBackground(Media... media) {

            List<Tombola> tombolaList = tombolaDao.getAllTombolas();

            for(Tombola tombola : tombolaList) {
                tombola.removeMedia(media[0]);
            }

            tombolaDao.updateAllTombolas(tombolaList);

            return null;
        }
    }
}