package com.example.tombolator.tombolas;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.tombolator.Converters;
import com.example.tombolator.media.Media;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Tombola implements Parcelable {

    @ColumnInfo
    @PrimaryKey
    private Long id;

    @ColumnInfo
    private long creationTimestamp;

    @ColumnInfo
    private String name;

    @TypeConverters({Converters.class})
    @ColumnInfo
    private List<Media> mediaAvailable;

    @TypeConverters({Converters.class})
    @ColumnInfo
    private List<Media> mediaDrawn;

    protected Tombola(String name) {

        this.name = name;
        this.creationTimestamp = System.currentTimeMillis();
    }

    protected Tombola(Parcel in) {

        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }

        creationTimestamp = in.readLong();
        name = in.readString();
    }

    public static final Creator<Tombola> CREATOR = new Creator<Tombola>() {
        @Override
        public Tombola createFromParcel(Parcel in) {
            return new Tombola(in);
        }

        @Override
        public Tombola[] newArray(int size) {
            return new Tombola[size];
        }
    };

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
    }

    public void addMedia(Media media) {
        mediaAvailable.add(media);
    }

    public void removeMedia(Media media) {
        mediaAvailable.remove(media);
    }

    public Media drawRandomMedia() {

        List<Media> mediaAvailableShuffled = new ArrayList<>(mediaAvailable);
        Collections.shuffle(mediaAvailableShuffled);

        return mediaAvailableShuffled.get(0);
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

    public List<Media> getMediaAvailable() {
        return mediaAvailable;
    }

    public void setMediaAvailable(List<Media> mediaAvailable) {
        this.mediaAvailable = mediaAvailable;
    }

    public List<Media> getMediaDrawn() {
        return mediaDrawn;
    }

    public void setMediaDrawn(List<Media> mediaDrawn) {
        this.mediaDrawn = mediaDrawn;
    }
}