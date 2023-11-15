package com.example.tombolator.commons;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.tombolator.R;
import com.example.tombolator.media.MediaUtil;

import java.util.List;
import java.util.function.Function;

/* TODO: Implement Sorting */
/* TODO: Implement Filtering */
public class PaginatedListComponent<T> extends ConstraintLayout {

    private static final int ELEMENTS_PER_PAGE = 6;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private LifecycleOwner lifecycleOwner;

    private LiveData<List<T>> items;

    private Function<T, String> itemToStringConverter;
    private OnClickListener itemOnClickListener;

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

        registerButtonListener();
    }

    private void setItems(LifecycleOwner lifecycleOwner, LiveData<List<T>> items) {

        this.lifecycleOwner = lifecycleOwner;
        this.items = items;

        registerObserver(this.lifecycleOwner, this.items);
    }

    private void registerObserver(LifecycleOwner lifecycleOwner, LiveData<List<T>> items) {

        items.observe(lifecycleOwner, this::showMediaOnCurrentPage);

        currentPage.observe(lifecycleOwner, new PageNumberCurrentObserver(items));
    }

    private void registerButtonListener() {

        nextPageButton.setOnClickListener((View view) -> {

            if (currentPage.getValue() == null)
                throw new IllegalStateException("Current page field should never be null!");

            if (items.getValue() == null)
                return;

            if (currentPage.getValue() == MediaUtil.getTotalNumberOfPages(items.getValue(), ELEMENTS_PER_PAGE))
                return;

            currentPage.setValue(currentPage.getValue() + 1);
        });

        previousPageButton.setOnClickListener((View view) -> {

            if (currentPage.getValue() == null)
                throw new IllegalStateException("Current page field should never be null!");

            if (currentPage.getValue() == 1)
                return;

            currentPage.setValue(currentPage.getValue() - 1);
        });
    }

    private void showMediaOnCurrentPage(List<T> items) {

        if (currentPage.getValue() == null)
            throw new IllegalStateException("Current page field should never be null!");

        int start = (currentPage.getValue() - 1) * ELEMENTS_PER_PAGE;
        int end = start + ELEMENTS_PER_PAGE;

        if (end > items.size())
            end = items.size();

        itemList.removeAllViews();

        for (int i = start; i < end; i++) {

            T item = items.get(i);

            TextView textView = (TextView) View.inflate(getContext(), R.layout.list_element, null);

            String text = itemToStringConverter == null ? item.toString() : itemToStringConverter.apply(item);

            Typeface defaultTypeface = textView.getTypeface();
            int defaultTextColor = textView.getCurrentTextColor();

            /* TODO: Implement here possibility to mark items that are used for anything. */
//            for (Media mediaInTombola : tombolasActivityViewModel.getSelectedTombola().getValue().getAllMedia()) {
//
//                if (id == mediaInTombola.getId()) {
//
//                    textView.setTextColor(Color.parseColor("#3700B3"));
//                    textView.setTypeface(defaultTypeface, Typeface.BOLD);
//                }
//            }

            textView.setText(text);

            if (itemOnClickListener != null)
                textView.setOnClickListener(itemOnClickListener);

            itemList.addView(textView);
        }
    }

    private void setItemToStringConverter(Function<T, String> itemToStringConverter) {
        this.itemToStringConverter = itemToStringConverter;
    }

    private void setItemOnClickListener(OnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    private class PageNumberCurrentObserver implements Observer<Integer> {

        private final LiveData<List<T>> items;

        public PageNumberCurrentObserver(LiveData<List<T>> items) {
            this.items = items;
        }

        @Override
        public void onChanged(Integer pageNumber) {

            currentPageLabel.setText(NumberUtil.formatNumberFullDigitsLeadingZero(pageNumber));
            showMediaOnCurrentPage(items.getValue());
        }
    }
}