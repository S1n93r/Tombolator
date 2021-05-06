package com.example.tombolator.tombolas;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface TombolaDao {

    @Query("SELECT id FROM Tombola")
    List<Long> getAllIds();

    @Query("SELECT * FROM Tombola")
    List<Tombola> getAllTombolas();

    @Query("SELECT * FROM Tombola")
    LiveData<List<Tombola>> getAllTombolasAsLiveData();

    @Query("SELECT * FROM Tombola WHERE id = :tombolaId")
    Tombola getTombola(long tombolaId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTombola(Tombola tombola);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTombolas(List<Tombola> tombolaList);

    @Update
    void updateTombola(Tombola tombola);

    @Update
    void updateAllTombolas(List<Tombola> tombolaList);

    @Delete
    void deleteMedia(Tombola tombola);

    @Query("DELETE FROM Tombola")
    void nukeTable();

}