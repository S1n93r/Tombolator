package com.example.tombolator.tombolas;

import androidx.room.*;

import java.util.List;

@Dao
public interface TombolaDao {

    @Query("SELECT id FROM Tombola")
    List<Long> getAllIds();

    @Query("SELECT * FROM Tombola WHERE id = :tombolaId")
    Tombola getById(long tombolaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTombola(Tombola tombola);

    @Delete
    void deleteMedia(Tombola tombola);

    @Query("DELETE FROM Tombola")
    void nukeTable();

}
