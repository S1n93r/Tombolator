package com.example.tombolator.commons;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tombolator.R;

public class PaginatedListEntry<T> extends RelativeLayout {

    private static final int FONT_COLOR_MEDIA_SELECTED = Color.parseColor("#3700B3");
    private static final int FONT_COLOR_MEDIA = Color.BLACK;
    private final MutableLiveData<Boolean> selected = new MutableLiveData<>(false);
    private TextView textView;
    private ImageView deleteIcon;
    private Runnable deleteRunnable = () -> {/* empty */};
    private Runnable clickTextRunnable = () -> {/* empty */};

    public PaginatedListEntry(@NonNull Context context) {

        super(context);

        configureComponent(context);
    }

    public PaginatedListEntry(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponent(context);
    }

    public PaginatedListEntry(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponent(context);
    }

    private void configureComponent(Context context) {

        if (selected.getValue() == null)
            throw new IllegalStateException("Value 'selected' should never be null!");

        View innerView = inflate(context, R.layout.paginated_list_entry, null);

        textView = innerView.findViewById(R.id.entry_name);
        textView.setOnClickListener(view -> clickTextRunnable.run());

        deleteIcon = innerView.findViewById(R.id.entry_delete_icon);
        deleteIcon.setOnClickListener(view -> deleteRunnable.run());

        addView(innerView);
    }

    public void initialize(LifecycleOwner lifecycleOwner) {

        selected.observe(lifecycleOwner, selected -> {

            if (selected) {
                textView.setTextColor(FONT_COLOR_MEDIA_SELECTED);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            } else {
                textView.setTextColor(FONT_COLOR_MEDIA);
                textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
            }
        });
    }

    public LiveData<Boolean> getSelected() {
        return selected;
    }

    public void select() {
        selected.setValue(true);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setDeleteRunnable(Runnable deleteRunnable) {
        this.deleteRunnable = deleteRunnable;
    }

    public void setClickTextRunnable(Runnable clickTextRunnable) {
        this.clickTextRunnable = clickTextRunnable;
    }
}
