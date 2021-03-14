package com.example.tombolator.media;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MediaDao {

    @Query("SELECT * FROM Media WHERE id := :id")
    public Media getById(int id);

    @Insert
    public int insertMedia(Media media);

    @Delete
    public void deleteMedia(Media media);

}