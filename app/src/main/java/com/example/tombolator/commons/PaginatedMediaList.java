package com.example.tombolator.commons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;
import com.example.tombolator.media.MediaUtil;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PaginatedMediaList extends ConstraintLayout {

    private static final int SORTING_NONE = 0;
    private static final int SORTING_REGULAR = 1;
    private static final int SORTING_REVERSED = 2;

    private static final int DEFAULT_ELEMENTS_PER_PAGE = 5;

    private static final int SINGLE_SELECT_SHOW_DETAILS = 0;
    private static final int MULTI_SELECT_MARK_MEDIA = 1;

    private static final String FILTER_ALL_CATEGORIES = "[show all categories]";

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private final MutableLiveData<String> selectedMediaType = new MutableLiveData<>(FILTER_ALL_CATEGORIES);

    private final MutableLiveData<Media> selectedMedia = new MutableLiveData<>();

    private final MutableLiveData<List<Media>> selectedMediaList = new MutableLiveData<>(new ArrayList<>());

    private MediaActivityViewModel mediaActivityViewModel;

    private int mode = SINGLE_SELECT_SHOW_DETAILS;

    private int currentSortingMode = SORTING_NONE;

    private boolean isConfigured = false;

    private LiveData<List<Media>> mediaList;

    private FragmentActivity fragmentActivity;

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
    private OnClickListener processMediaListButtonListener;

    public PaginatedMediaList(@NonNull Context context) {

        super(context);

        initView(context, null);
    }

    public PaginatedMediaList(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        initView(context, attrs);
    }

    public void configureView(FragmentActivity fragmentActivity, OnClickListener backButtonListener, OnClickListener processMediaListButtonListener) {

        mediaActivityViewModel = new ViewModelProvider(fragmentActivity).get(MediaActivityViewModel.class);

        this.fragmentActivity = fragmentActivity;
        this.mediaList = mediaActivityViewModel.getAllMediaFilteredAndSortedLiveData();
        this.backButtonListener = backButtonListener;
        this.processMediaListButtonListener = processMediaListButtonListener;

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

        if (attrs != null) {

            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.PaginatedMediaList, 0, 0);

            titleTextView.setText(a.getString(R.styleable.PaginatedMediaList_title));
            elementsPerPage = a.getInteger(R.styleable.PaginatedMediaList_elementsPerPage, DEFAULT_ELEMENTS_PER_PAGE);
            processMediaListButton.setText(a.getString(R.styleable.PaginatedMediaList_processButtonText));

            Drawable drawable = a.getDrawable(R.styleable.PaginatedMediaList_processButtonIcon);

            processMediaListButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);

            mode = a.getInteger(R.styleable.PaginatedMediaList_mode, SINGLE_SELECT_SHOW_DETAILS);

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

        selectedMediaType.observe(fragmentActivity, string -> {

            /* TODO: NPE because media was not yet loaded from room. Why? */
            if (mediaList.getValue() == null)
                return;

            Collection<Media> filteredCollection = Collections2.filter(
                    mediaList.getValue(), new MediaTypeFilterPredicate(selectedMediaType.getValue()));

            showMediaOnCurrentPage(new ArrayList<>(filteredCollection));
        });

        mediaList.observe(fragmentActivity, media -> {

            int numberOfPages = MediaUtil.getTotalNumberOfPages(media, elementsPerPage);

            pageNumberMax.setText(NumberUtil.formatNumberFullDigitsLeadingZero(numberOfPages));

            showMediaOnCurrentPage(media);
        });

        currentPage.observe(fragmentActivity, integer -> {

            pageNumberCurrent.setText(NumberUtil.formatNumberFullDigitsLeadingZero(integer));

            showMediaOnCurrentPage(mediaList.getValue());
        });
    }

    private void setUpListener() {

        backButton.setOnClickListener(backButtonListener);

        nextPageButton.setOnClickListener(v -> {

            if (currentPage.getValue() == null)
                throw new IllegalStateException("Value of live data \"currentPage\" is null." +
                        " That should never be the case.");

            if (mediaList.getValue() == null)
                throw new IllegalStateException("Media list should not be null.");

            if (currentPage.getValue() ==
                    MediaUtil.getTotalNumberOfPages(mediaList.getValue(), elementsPerPage))
                return;

            currentPage.setValue(currentPage.getValue() + 1);
        });

        previousPageButton.setOnClickListener(v -> {

            if (currentPage.getValue() == null)
                throw new IllegalStateException("Value of live data \"currentPage\" is null." +
                        " That should never be the case.");

            if (currentPage.getValue() == 1)
                return;

            currentPage.setValue(currentPage.getValue() - 1);
        });

        sortButton.setOnClickListener(v -> mediaActivityViewModel.toggleSortingMode());

        processMediaListButton.setOnClickListener(processMediaListButtonListener);
    }

    private void checkConfiguration() {
        if (!isConfigured)
            throw new IllegalStateException("Please call configureView() before you start using this view.");
    }

    private void showMediaOnCurrentPage(List<Media> mediaList) {

        if (selectedMediaList.getValue() == null) {
            throw new IllegalStateException("Selected media list should not be null at this point.");
        }

        if (currentPage.getValue() == null) {
            throw new IllegalStateException("Current page should not be null at this point.");
        }

        int start = (currentPage.getValue() - 1) * elementsPerPage;
        int end = start + elementsPerPage;

        if (end > mediaList.size())
            end = mediaList.size();

        linearLayoutMedia.removeAllViews();

        for (int i = start; i < end; i++) {

            Media media = mediaList.get(i);

            long id = media.getId();

            MediaEntryTextView textView = (MediaEntryTextView) View.inflate(
                    getContext(), R.layout.list_element, null);

            textView.setMedia(media);

            String text = " " + media.toLabel();

            textView.setText(text);
            textView.setId((int) id);

            Typeface defaultTypeface = textView.getTypeface();
            int defaultTextColor = textView.getCurrentTextColor();

            for (Media mediaInTombola : selectedMediaList.getValue()) {

                if (id == mediaInTombola.getId()) {

                    textView.setTextColor(Color.parseColor("#3700B3"));
                    textView.setTypeface(defaultTypeface, Typeface.BOLD);
                }
            }

            textView.setOnClickListener(new MediaEntryListener(defaultTypeface, defaultTextColor));

            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    MediaUtil.getMediaTypeIcon(media), 0, 0, 0);

            linearLayoutMedia.addView(textView);
        }
    }

    public MutableLiveData<Media> getSelectedMedia() {
        return selectedMedia;
    }

    public MutableLiveData<List<Media>> getSelectedMediaList() {
        return selectedMediaList;
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

            if (mediaType.equals(FILTER_ALL_CATEGORIES))
                return true;

            return mediaType.equals(media.getMediaType());
        }
    }

    private class MediaTypeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (i == 0)
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

    private class MediaEntryListener implements OnClickListener {

        private final Typeface defaultTypeface;
        private final int defaultTextColor;

        public MediaEntryListener(Typeface defaultTypeface, int defaultTextColor) {
            this.defaultTypeface = defaultTypeface;
            this.defaultTextColor = defaultTextColor;
        }

        @Override
        public void onClick(View view) {

            if (selectedMediaList.getValue() == null)
                throw new IllegalStateException("Selected media list should not be null at this point.");

            if (!(view instanceof MediaEntryTextView))
                throw new IllegalStateException("The view you are trying to add is not of type MediaEntryTextView.");

            MediaEntryTextView mediaEntryTextView = (MediaEntryTextView) view;
            Media media = mediaEntryTextView.getMedia();

            switch (mode) {

                case SINGLE_SELECT_SHOW_DETAILS:

                    selectedMedia.setValue(media);
                    break;

                case MULTI_SELECT_MARK_MEDIA:

                    if (selectedMediaList.getValue().contains(media)) {

                        selectedMediaList.getValue().remove(media);
                        mediaEntryTextView.setTextColor(defaultTextColor);
                        mediaEntryTextView.setTypeface(defaultTypeface);
                    } else {

                        selectedMediaList.getValue().add(media);
                        mediaEntryTextView.setTextColor(Color.parseColor("#3700B3"));
                        mediaEntryTextView.setTypeface(defaultTypeface, Typeface.BOLD);
                    }

                    break;

                default:
                    throw new SwitchCaseNotDefinedException("");
            }
        }
    }
}