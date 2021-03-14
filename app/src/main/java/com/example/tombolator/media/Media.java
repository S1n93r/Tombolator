package com.example.tombolator.media;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Media {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private final int id;

    @ColumnInfo
    private final Date creationDate;

    private String name;

    protected Media(int id, String name) {

        this.id = id;
        creationDate = new Date(System.currentTimeMillis());
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + id + ") " + name;
    }
}