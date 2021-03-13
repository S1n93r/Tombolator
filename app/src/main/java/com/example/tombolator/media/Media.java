package com.example.tombolator.media;

import androidx.annotation.NonNull;

import java.util.Date;

public class Media {

    private final int id;
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