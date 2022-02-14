package com.example.tombolator.commons;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.tombolator.media.Media;
import lombok.Getter;
import lombok.Setter;

public class MediaEntryTextView extends androidx.appcompat.widget.AppCompatTextView {

    @Getter
    @Setter
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
}
