package com.example.tombolator.media;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT id FROM Media")
    List<Long> getAllIds();

    @Query("SELECT * FROM Media")
    List<Media> getAllMedia();

    @Query("SELECT * FROM Media")
    LiveData<List<Media>> getAllMediaAsLiveData();

    @Query("SELECT * FROM Media WHERE id = :mediaId")
    Media getMedia(long mediaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedia(Media media);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMedia(List<Media> mediaList);

    @Update
    void updateMedia(Media media);

    @Update
    void updateAllMedia(List<Media> mediaList);

    /* Linter can be ignored. Will be used as soon as media details view is implemented. */
    @Delete
    void deleteMedia(Media media);

    @Query("DELETE FROM Media")
    void nukeTable();
    
}