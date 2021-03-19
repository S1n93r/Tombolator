package com.example.tombolator.media;

import androidx.room.*;

import java.util.List;

@Dao
public interface MediaDao {

    @Query("SELECT id FROM Media")
    List<Integer> getAllIds();

    @Query("SELECT * FROM Media WHERE id = :mediaId")
    Media getById(int mediaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedia(Media media);

    @Delete
    void deleteMedia(Media media);

    @Query("DELETE FROM Media")
    void nukeTable();

}