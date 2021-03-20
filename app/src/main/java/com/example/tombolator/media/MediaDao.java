package com.example.tombolator.media;

import androidx.room.*;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT id FROM Media")
    List<Long> getAllIds();

    @Query("SELECT * FROM Media WHERE id = :mediaId")
    Media getById(long mediaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedia(Media media);

    /* Linter can be ignored. Will be used as soon as media details view is implemented. */
    @Delete
    void deleteMedia(Media media);

    @Query("DELETE FROM Media")
    void nukeTable();
    
}