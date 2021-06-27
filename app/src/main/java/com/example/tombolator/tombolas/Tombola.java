package com.example.tombolator.tombolas;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.*;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaListConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Entity
public class Tombola implements Parcelable {

    @ColumnInfo
    @PrimaryKey
    private Long id;

    @ColumnInfo
    private long creationTimestamp;

    @ColumnInfo
    private String name;

    @TypeConverters({com.example.tombolator.tombolas.TombolaTypeConverter.class})
    @ColumnInfo
    private Type type;

    @TypeConverters({MediaListConverter.class})
    @ColumnInfo
    private List<Media> mediaAvailable = new ArrayList<>();

    @TypeConverters({MediaListConverter.class})
    @ColumnInfo
    private List<Media> mediaDrawn = new ArrayList<>();

    @Ignore
    public Tombola() {}

    protected Tombola(String name) {

        this.name = name;
        this.creationTimestamp = System.currentTimeMillis();
        this.type = Type.REUSE;
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

    public void removeMedia(long mediaId) {
        mediaAvailable.removeIf(new MediaIdsComparePredicate(mediaId));
        mediaDrawn.removeIf(new MediaIdsComparePredicate(mediaId));
    }

    public void removeMedia(Media media) {
        removeMedia(media.getId());
        removeMedia(media.getId());
    }

    public Media drawRandomMedia() {

        switch(type) {

            default:
            case REUSE: return drawRandomMediaAndReuse();
            case REMOVE:
            case DELETE: return drawRandomMediaAndRemove();
        }
    }

    private Media drawRandomMediaAndReuse() {

        if(mediaAvailable.isEmpty() && mediaDrawn.isEmpty())
            return null;

        if(mediaAvailable.isEmpty())
            resetMediaDrawnToMediaAvailable();

        List<Media> mediaAvailableShuffled = new ArrayList<>(mediaAvailable);

        Collections.shuffle(mediaAvailableShuffled);

        Media media = mediaAvailableShuffled.get(0);

        mediaAvailable.remove(media);
        mediaDrawn.add(media);

        return media;
    }

    private Media drawRandomMediaAndRemove() {

        if(mediaAvailable.isEmpty() && mediaDrawn.isEmpty())
            return null;

        List<Media> mediaAvailableShuffled = new ArrayList<>(mediaAvailable);

        Collections.shuffle(mediaAvailableShuffled);

        Media media = mediaAvailableShuffled.get(0);

        mediaAvailable.remove(media);

        return media;
    }

    private void resetMediaDrawnToMediaAvailable() {
        mediaAvailable.addAll(mediaDrawn);
        mediaDrawn.clear();
    }

    public String toLabel() {
        return name + " (" + getAllMedia().size() + " Medien)";
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Media> getAllMedia() {

        List<Media> allMedia = new ArrayList<>();
        allMedia.addAll(mediaAvailable);
        allMedia.addAll(mediaDrawn);

        return allMedia;
    }

    public enum Type {

        REUSE ("wiederverwenden", "Medien verbleiben nach dem Ziehen in der Tombola."),
        REMOVE ("entfernen", "Medien werden nach dem Ziehen aus der Tombola entfernt."),
        DELETE ("löschen", "Medien werden nach dem Ziehen aus der Tombola entfernt und gelöscht.");

        final String description;
        final String toolTip;

        Type(String description, String toolTip) {
            this.description = description;
            this.toolTip = toolTip;
        }

        public static Type getTypeByDescription(String description) {

            if(description.equals(DELETE.description))
                return DELETE;
            else if(description.equals(REMOVE.description))
                return REMOVE;
            else
                return REUSE;
        }
    }

    private static class MediaIdsComparePredicate implements Predicate<Media> {

        private final long mediaId;

        public MediaIdsComparePredicate(long mediaId) {
            this.mediaId = mediaId;
        }

        @Override
        public boolean test(Media media) {
            return mediaId == media.getId();
        }
    }
}