package com.example.tombolator.commons;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tombolator.media.Media;

public class MediaEntryTextView extends androidx.appcompat.widget.AppCompatTextView {

    private Media media;

    public MediaEntryTextView(@NonNull Context context) {
        super(context);
    }

    public MediaEntryTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaEntryTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
