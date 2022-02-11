package com.example.tombolator.media;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

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
    @Getter
    @Setter
    private Long id;

    @ColumnInfo
    @Getter
    @Setter
    private long creationTimestamp;

    @ColumnInfo
    @Getter
    @Setter
    private String name;

    @ColumnInfo
    @Getter
    @Setter
    private String title;

    @ColumnInfo
    @Getter
    @Setter
    private int number;

    @ColumnInfo
    @Getter
    @Setter
    private String author;

    @ColumnInfo
    @Getter
    @Setter
    private String mediaType;

    @ColumnInfo
    @Getter
    @Setter
    private String contentType;

    @Ignore
    public Media() {}

    public Media(String name, String title, int number, String author, String mediaType, String contentType) {

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
        mediaType = in.readString();
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
        parcel.writeString(mediaType);
    }

    public String toLabel() {

        if(number == -1)
            return name + "\n" + title;

        return "(" + number + ") "+ name + "\n" + title;
    }

    public String toCsv() {

        String id = this.id != null ? this.id.toString() : "";
        String creationTimestamp = String.valueOf(this.creationTimestamp);
        String name = this.name != null ? this.name : "";
        String title = this.title != null ? this.title : "";
        String number = String.valueOf(this.number);
        String author = this.author != null ? this.author : "";
        String mediaType = this.mediaType != null ? this.mediaType : "";
        String contenType = this.contentType != null ? this.contentType : "";

        return id + ";" + creationTimestamp + ";" + name + ";" + title + ";" + number + ";" + author + ";"
                + mediaType +  ";" + contenType + ";";
    }

    public static final class MediaType {

        public static final String CASSETTE = "Kassette";
        public static final String CD = "CD";
        public static final String DVD = "DVD";
        public static final String BLU_RAY = "Blu-ray";
        public static final String E_BOOK = "E-Book";
        public static final String BOOK = "Buch";
        public static final String STREAMING = "Streaming";
        public static final String MEAL = "Essen";

        public static List<String> getMediaTypes() {

            List<String> mediaTypes = new ArrayList<>();
            Collections.addAll(mediaTypes, CASSETTE, CD, DVD, BLU_RAY, E_BOOK, BOOK, STREAMING, MEAL);

            return mediaTypes;
        }

        public static int getIndex(String type) {

            switch(type) {
                case CASSETTE:
                default: return 0;
                case CD: return 1;
                case DVD: return 2;
                case BLU_RAY: return 3;
                case E_BOOK: return 4;
                case BOOK: return 5;
                case STREAMING: return 6;
                case MEAL: return 7;
            }
        }

        public static String getMediaType(int index) {

            switch(index) {
                case 0:
                default: return CASSETTE;
                case 1: return CD;
                case 2: return DVD;
                case 3: return BLU_RAY;
                case 4: return E_BOOK;
                case 5: return BOOK;
                case 6: return STREAMING;
                case 7: return MEAL;
            }
        }
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

            switch(type) {
                case AUDIO_PLAY:
                default: return 0;
                case MOVIE: return 1;
                case SERIES: return 2;
            }
        }
    }
}