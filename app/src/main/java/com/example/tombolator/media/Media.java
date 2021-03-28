package com.example.tombolator.media;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Media implements Parcelable {

    @ColumnInfo
    @PrimaryKey
    private Long id;

    @ColumnInfo
    private long creationTimestamp;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String title;

    @ColumnInfo
    private int number;

    @ColumnInfo
    private String type;

    public Media(String name, String title, int number, String type) {

        creationTimestamp = System.currentTimeMillis();
        this.name = name;
        this.title = title;
        this.number = number;
        this.type = type;
    }

    public Media(Parcel in) {

        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }

        creationTimestamp = in.readLong();
        name = in.readString();
        title = in.readString();
        number = in.readInt();
        type = in.readString();
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
    public void writeToParcel(Parcel parcel, int i) {

        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);

        }

        parcel.writeLong(creationTimestamp);
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeInt(number);
        parcel.writeString(type);
    }

    public String toLabel() {
        return "(" + number + ") "+ name + " - " + title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static final class Type {

        public static final String CASSETTE = "Kassette";
        public static final String CD = "CD";
        public static final String DVD = "DVD";
        public static final String BLU_RAY = "Blu-ray";
        public static final String E_BOOK = "E-Book";
        public static final String BOOK = "Buch";
    }

    public static final class ContentType {

        public static final String AUDIO_PLAY = "Hörspiel";
        public static final String AUDIO_BOOK = "Hörbuch";
        public static final String MOVIE = "Film";
        public static final String SERIES = "Serie";
        public static final String BOOK = Type.BOOK;
        public static final String E_BOOK = Type.E_BOOK;
    }
}