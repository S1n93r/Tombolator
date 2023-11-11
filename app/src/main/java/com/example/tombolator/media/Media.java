package com.example.tombolator.media;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Media implements Parcelable {

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
    private String author;

    @ColumnInfo
    @TypeConverters({MediaTypeConverter.class})
    private MediaType mediaType;

    @ColumnInfo
    private String contentType;

    @Ignore
    public Media() {
    }

    public Media(String name, String title, int number, String author, MediaType mediaType, String contentType) {

        creationTimestamp = System.currentTimeMillis();
        this.name = name;
        this.title = title;
        this.number = number;
        this.author = author;
        this.mediaType = mediaType;
        this.contentType = contentType;
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

        String mediaTypeString = in.readString();

        if (mediaTypeString == null)
            throw new IllegalStateException("Media type string should not be empty in media parcel.");

        mediaType = MediaType.valueOf(MediaType.class, mediaTypeString);
    }

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
        parcel.writeString(mediaType.name());
    }

    public String toLabel() {

        if (number == -1)
            return name + "\n" + title;

        return "(" + number + ") " + name + "\n" + title;
    }

    public String toCsv() {

        String id = this.id != null ? this.id.toString() : "";
        String creationTimestamp = String.valueOf(this.creationTimestamp);
        String name = this.name != null ? this.name : "";
        String title = this.title != null ? this.title : "";
        String number = String.valueOf(this.number);
        String author = this.author != null ? this.author : "";
        String mediaType = this.mediaType != null ? this.mediaType.name() : "";
        String contenType = this.contentType != null ? this.contentType : "";

        return id + ";" + creationTimestamp + ";" + name + ";" + title + ";" + number + ";" + author + ";"
                + mediaType + ";" + contenType + ";";
    }

    /* Getters & Setters*/
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

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public static final class ContentType {

        public static final String AUDIO_PLAY = "Hörspiel";
        public static final String MOVIE = "Film";
        public static final String SERIES = "Serie";

        public static List<String> getContentTypes() {

            List<String> mediaTypes = new ArrayList<>();
            Collections.addAll(mediaTypes, AUDIO_PLAY, MOVIE, SERIES);

            return mediaTypes;
        }

        public static int getIndex(String type) {

            switch (type) {
                case AUDIO_PLAY:
                default:
                    return 0;
                case MOVIE:
                    return 1;
                case SERIES:
                    return 2;
            }
        }
    }
}