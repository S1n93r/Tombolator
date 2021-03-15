package com.example.tombolator.media;

import androidx.room.*;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM Media WHERE id = :mediaId")
    Media getById(int mediaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedia(Media media);

    @Delete
    void deleteMedia(Media media);

}