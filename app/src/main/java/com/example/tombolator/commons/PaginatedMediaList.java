package com.example.tombolator.commons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PaginatedMediaList extends ConstraintLayout {

    private static final int SORTING_NONE = 0;
    private static final int SORTING_REGULAR = 1;
    private static final int SORTING_REVERSED = 2;

    private static final int DEFAULT_ELEMENTS_PER_PAGE = 5;

    private static final String FILTER_ALL_CATEGORIES = "[show all categories]";

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private final MutableLiveData<String> selectedMediaType = new MutableLiveData<>(FILTER_ALL_CATEGORIES);

    private final MutableLiveData<List<Media>> filteredMediaList = new MutableLiveData<>(new ArrayList<>());

    private int currentSortingMode = SORTING_NONE;

    private boolean isConfigured = false;

    private LiveData<List<Media>> mediaList;

    private LifecycleOwner lifecycleOwner;

    private TextView titleTextView;

    private ImageView sortButton;
    private Spinner mediaTypesSpinner;
    private LinearLayout linearLayoutMedia;

    private Button nextPageButton;
    private TextView pageNumberCurrent;
    private TextView pageNumberMax;
    private Button previousPageButton;

    private Button backButton;

    private Button processMediaListButton;

    private int elementsPerPage;

    private OnClickListener backButtonListener;

    public PaginatedMediaList(@NonNull Context context) {

        super(context);

        initView(context, null);
    }

    public PaginatedMediaList(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        initView(context, attrs);
    }

    public void configureView(LifecycleOwner lifecycleOwner, LiveData<List<Media>> mediaList, OnClickListener backButtonListener) {

        this.lifecycleOwner = lifecycleOwner;
        this.mediaList = mediaList;
        this.backButtonListener = backButtonListener;

        isConfigured = true;

        setUpBindings();
        setUpListener();
    }

    private void initView(@NonNull Context context, @Nullable AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.paginated_media_list, this, true);

        titleTextView = view.findViewById(R.id.text_view_title);

        linearLayoutMedia = view.findViewById(R.id.linear_layout_media);

        mediaTypesSpinner = view.findViewById(R.id.spinner_media_types);

        pageNumberCurrent = view.findViewById(R.id.label_page_number_current);
        pageNumberMax = view.findViewById(R.id.label_page_number_total);

        sortButton = view.findViewById(R.id.button_sort_by);
        backButton = view.findViewById(R.id.back_button);
        nextPageButton = view.findViewById(R.id.button_next_page);
        previousPageButton = view.findViewById(R.id.button_previous_page);
        processMediaListButton = view.findViewById(R.id.button_process_media_list);

        handleAttributes(context, attrs);

        setUpMediaTypesSpinner();
    }

    private void handleAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {

        if(attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.PaginatedMediaList, 0, 0);

            titleTextView.setText(a.getString(R.styleable.PaginatedMediaList_title));
            elementsPerPage = a.getInteger(R.styleable.PaginatedMediaList_elementsPerPage, DEFAULT_ELEMENTS_PER_PAGE);
            processMediaListButton.setText(a.getString(R.styleable.PaginatedMediaList_processButtonText));

            Drawable drawable = a.getDrawable(R.styleable.PaginatedMediaList_processButtonIcon);

            processMediaListButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

            a.recycle();
        }
    }

    private void setUpMediaTypesSpinner() {

        List<String> mediaTypesForSpinner = Media.MediaType.getMediaTypes();
        mediaTypesForSpinner.add(0, "Alle");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(), R.layout.media_type_spinner_item, mediaTypesForSpinner);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);
        mediaTypesSpinner.setAdapter(arrayAdapter);

        mediaTypesSpinner.setOnItemSelectedListener(new MediaTypeItemSelectedListener());
    }

    private void setUpBindings() {

        checkConfiguration();

        selectedMediaType.observe(lifecycleOwner, string -> {

            /* TODO: NPE because media was not yet loaded from room. Why? */
            if(mediaList.getValue() == null)
                return;

            Collection<Media> filteredCollection = Collections2.filter(
                    mediaList.getValue(), new MediaTypeFilterPredicate(selectedMediaType.getValue()));

            filteredMediaList.setValue(new ArrayList<>(filteredCollection));
        });

        filteredMediaList.observe(lifecycleOwner, media -> {

            int numberOfPages = MediaUtil.getTotalNumberOfPages(media, elementsPerPage);

            pageNumberMax.setText(NumberUtil.formatNumberFullDigitsLeadingZero(numberOfPages));

            showMediaOnCurrentPage(filteredMediaList.getValue());
        });

        currentPage.observe(lifecycleOwner, integer -> {

            pageNumberCurrent.setText(NumberUtil.formatNumberFullDigitsLeadingZero(integer));

            showMediaOnCurrentPage(filteredMediaList.getValue());
        });
    }

    private void setUpListener() {

        backButton.setOnClickListener(backButtonListener);

        nextPageButton.setOnClickListener(v -> {

            if(currentPage.getValue() == null)
                throw new IllegalStateException("Value of live data \"currentPage\" is null." +
                        " That should never be the case.");

            if(filteredMediaList.getValue() == null)
                throw new IllegalStateException("Value of live data \"filteredMediaList\" is null. " +
                        "That should never be the case.");

            if(currentPage.getValue() ==
                    MediaUtil.getTotalNumberOfPages(filteredMediaList.getValue(), elementsPerPage))
                return;

            currentPage.setValue(currentPage.getValue() + 1);
        });

        previousPageButton.setOnClickListener(v -> {

            if(currentPage.getValue() == null)
                throw new IllegalStateException("Value of live data \"currentPage\" is null." +
                        " That should never be the case.");

            if(currentPage.getValue() == 1)
                return;

            currentPage.setValue(currentPage.getValue() - 1);
        });

        sortButton.setOnClickListener(v -> toggleSorting());
    }

    private void checkConfiguration() {
        if(!isConfigured)
            throw new IllegalStateException("Please call configureView() before you start using this view.");
    }

    private void showMediaOnCurrentPage (List<Media> mediaList) {

        if(currentPage.getValue() == null) {
            /* TODO: Log NPE here. */
            throw new NullPointerException();
        }

        int start = (currentPage.getValue() - 1) * elementsPerPage;
        int end = start + elementsPerPage;

        if(end > mediaList.size())
            end = mediaList.size();

        linearLayoutMedia.removeAllViews();

        for(int i=start; i<end; i++) {

            Media media = mediaList.get(i);

            long id = media.getId();

            TextView textView = (TextView) View.inflate(
                    getContext(), R.layout.list_element, null);

            String text = " " + media.toLabel();

            textView.setText(text);
            textView.setId((int) id);

            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    MediaUtil.getMediaTypeIcon(media), 0, 0, 0);

            linearLayoutMedia.addView(textView);
        }
    }

    private void toggleSorting() {

        switch(currentSortingMode) {

            case SORTING_NONE:
                currentSortingMode = SORTING_REGULAR;
                break;
            case SORTING_REGULAR:
                currentSortingMode = SORTING_REVERSED;
                break;
            case SORTING_REVERSED:
            default:
                currentSortingMode = SORTING_NONE;
        }

        applySorting();
    }

    private void applySorting() {

        if(filteredMediaList.getValue() == null) {
            /* TODO: Add log entry. */
            throw new NullPointerException();
        }

        filteredMediaList.getValue().sort(new MediaComparator());

        switch(currentSortingMode) {

            case SORTING_REGULAR:
                filteredMediaList.getValue().sort(new MediaComparator());
                break;

            case SORTING_REVERSED:
                filteredMediaList.getValue().sort(new MediaComparator().reversed());
                break;

            case SORTING_NONE:
            default: /* TODO: Implement default sorting. */
        }

        /* TODO: Re-set necessary? */
        filteredMediaList.postValue(filteredMediaList.getValue());
    }

    private static class MediaTypeFilterPredicate implements Predicate<Media> {

        private final String mediaType;

        public MediaTypeFilterPredicate(String mediaType) {
            this.mediaType = mediaType;
        }

        @Override
        public boolean apply(@NullableDecl Media media) {

            if (media == null) {
                /* TODO: Add error log here */
                throw new NullPointerException();
            }

            if(mediaType.equals(FILTER_ALL_CATEGORIES))
                return true;

            return mediaType.equals(media.getMediaType());
        }
    }

    private static class MediaComparator implements Comparator<Media> {

        @Override
        public int compare(Media m1, Media m2) {

            String titleAndName1 = m1.getName() + m1.getTitle();
            String titleAndName2 = m2.getName() + m2.getTitle();

            return titleAndName1.compareTo(titleAndName2);
        }
    }

    private class MediaTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if(i == 0)
                selectedMediaType.setValue(FILTER_ALL_CATEGORIES);
            else
                selectedMediaType.setValue(Media.MediaType.getMediaType(i - 1));

            currentPage.setValue(1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            /* TODO: When is this triggered? */
        }
    }
}