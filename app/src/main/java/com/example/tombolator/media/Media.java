package com.example.tombolator.media;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Media implements Parcelable {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private final int id;

    @ColumnInfo
    private final Date creationDate = new Date(System.currentTimeMillis());

    private String name;

    protected Media(int id, String name) {

        this.id = id;
        this.name = name;
    }

    protected Media(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    @NonNull
    @Override
    public String toString() {
        return "(" + id + ") " + name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}