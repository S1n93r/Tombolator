package com.example.tombolator.tombolas;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tombola implements Parcelable {

    @ColumnInfo
    @PrimaryKey
    private Long id;

    @ColumnInfo
    private long creationTimestamp = System.currentTimeMillis();

    @ColumnInfo
    private String name;

    protected Tombola(Long creationTimestamp, String name) {

        this.name = name;
        this.creationTimestamp = creationTimestamp;
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
}