package com.example.tombolator.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.example.tombolator.R;

import java.util.ArrayList;
import java.util.List;

public class PaginatedListComponent<T> extends ConstraintLayout {

    private final MutableLiveData<List<T>> itemsLiveData = new MutableLiveData<>(new ArrayList<>());

    private LifecycleOwner lifecycleOwner;

    private ImageView sortButton;

    private Spinner mediaTypesSpinner;

    private LinearLayout itemList;

    /* Page number */
    private ImageView previousPageButton;
    private TextView currentPageLabel;
    private TextView maxPageLabel;
    private ImageView nextPageButton;

    public PaginatedListComponent(Context context) {

        super(context);

        configureComponents(context);
    }

    public PaginatedListComponent(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        configureComponents(context);
    }

    public PaginatedListComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        configureComponents(context);
    }

    public PaginatedListComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);

        configureComponents(context);
    }

    private void configureComponents(Context context) {

        View innerView = inflate(context, R.layout.paginated_list_component, null);

        sortButton = innerView.findViewById(R.id.button_sort_by);

        mediaTypesSpinner = innerView.findViewById(R.id.spinner_media_types);

        itemList = innerView.findViewById(R.id.item_list);

        /* Page number */
        previousPageButton = innerView.findViewById(R.id.button_previous_page);
        currentPageLabel = innerView.findViewById(R.id.label_current_page);
        maxPageLabel = innerView.findViewById(R.id.label_max_page);
        nextPageButton = innerView.findViewById(R.id.button_next_page);

        this.addView(innerView);
    }
}