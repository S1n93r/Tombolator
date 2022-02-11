package com.example.tombolator.commons;

import android.content.Context;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import com.example.tombolator.R;
import lombok.Setter;

public class PaginatedMediaList extends ConstraintLayout {

    private static final int ELEMENTS_PER_PAGE = 6;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    @Setter
    private View.OnClickListener showDetailsListener;

    private LinearLayout linearLayoutMedia;

    private Spinner mediaTypesSpinner;

    private TextView pageNumberCurrent;
    private TextView pageNumberMax;

    private ImageView sortButton;

    private Button backButton;
    private Button nextPageButton;
    private Button previousPageButton;
    private Button newMediaButton;

    public PaginatedMediaList(@NonNull Context context) {

        super(context);

        inflate(getContext(), R.layout.paginated_media_list, null);
    }
}