package com.example.tombolator.commons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PaginatedListEntry<T> extends androidx.appcompat.widget.AppCompatTextView {

    private static final int FONT_COLOR_MEDIA_SELECTED = Color.parseColor("#3700B3");
    private static final int FONT_COLOR_MEDIA = Color.BLACK;

    private final MutableLiveData<Boolean> selected = new MutableLiveData<>(false);

    public PaginatedListEntry(@NonNull Context context) {

        super(context);

        configureComponent();
    }

    public PaginatedListEntry(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponent();
    }

    public PaginatedListEntry(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponent();
    }

    private void configureComponent() {

        if (selected.getValue() == null)
            throw new IllegalStateException("Value 'selected' should never be null!");

        setOnClickListener(view -> selected.setValue(!selected.getValue()));
    }

    public void initialize(LifecycleOwner lifecycleOwner) {

        selected.observe(lifecycleOwner, selected -> {

            if (selected) {
                setTextColor(FONT_COLOR_MEDIA_SELECTED);
                setTypeface(getTypeface(), Typeface.BOLD);
            } else {
                setTextColor(FONT_COLOR_MEDIA);
                setTypeface(getTypeface(), Typeface.NORMAL);
            }
        });
    }

    public LiveData<Boolean> getSelected() {
        return selected;
    }

    public void select() {
        selected.setValue(true);
    }
}
