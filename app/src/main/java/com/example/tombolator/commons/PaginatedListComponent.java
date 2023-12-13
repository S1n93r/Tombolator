package com.example.tombolator.commons;

import static com.example.tombolator.media.SortingMode.A_TO_Z;
import static com.example.tombolator.media.SortingMode.Z_TO_A;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.tombolator.media.SortingMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PaginatedListComponent<T> extends ConstraintLayout {

    /* FIXME: Not working as component does not expand if there are more items. */
    private static final int ELEMENTS_PER_PAGE = 8;

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private final MutableLiveData<SortingMode> sortingMode = new MutableLiveData<>(A_TO_Z);

    private final MutableLiveData<String> selectedFilter = new MutableLiveData<>(null);
    private final MutableLiveData<List<T>> items = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<T>> itemsSortedAndFiltered = new MutableLiveData<>(new ArrayList<>());
    private Function<T, String> filterValueExtractor;
    private List<String> filterValues = new ArrayList<>();
    private Function<T, String> itemSortingStringConverter;
    private Function<T, View> itemToViewConverter;

    private ImageView sortButton;

    private Spinner itemFilterSpinner;

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

        itemFilterSpinner = innerView.findViewById(R.id.spinner_media_types);

        itemList = innerView.findViewById(R.id.item_list);

        /* Page number */
        previousPageButton = innerView.findViewById(R.id.button_previous_page);
        currentPageLabel = innerView.findViewById(R.id.label_current_page);
        maxPageLabel = innerView.findViewById(R.id.label_max_page);
        nextPageButton = innerView.findViewById(R.id.button_next_page);

        this.addView(innerView);

        registerButtonListener();
    }

    public void setItems(LifecycleOwner lifecycleOwner, LiveData<List<T>> items) {

        registerObserver(lifecycleOwner, items);
    }

    private void registerObserver(LifecycleOwner lifecycleOwner, LiveData<List<T>> items) {

        items.observe(lifecycleOwner, this.items::setValue);
        items.observe(lifecycleOwner, newItems -> applySortingAndFiltering());

        currentPage.observe(lifecycleOwner, new PageNumberCurrentObserver());
        itemsSortedAndFiltered.observe(lifecycleOwner, this::showMediaOnCurrentPage);
        itemsSortedAndFiltered.observe(lifecycleOwner, new PageNumberTotalObserver());

        sortingMode.observe(lifecycleOwner, newSortingMode -> applySortingAndFiltering());
        selectedFilter.observe(lifecycleOwner, newSelectedFilter -> applySortingAndFiltering());
    }

    private void toggleSorting() {

        if (sortingMode.getValue() == A_TO_Z)
            sortingMode.setValue(Z_TO_A);
        else
            sortingMode.setValue(A_TO_Z);
    }

    private void applySortingAndFiltering() {
        itemsSortedAndFiltered.setValue(filter(sort(items.getValue())));
    }

    private List<T> sort(List<T> items) {

        List<T> itemsToSort = new ArrayList<>(items);

        if (sortingMode.getValue() == null)
            return itemsToSort;

        switch (sortingMode.getValue()) {

            case A_TO_Z:
                itemsToSort.sort(new ItemComparator());
                break;

            case Z_TO_A:
                itemsToSort.sort(new ItemComparator().reversed());
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + sortingMode);
        }

        return itemsToSort;
    }

    private List<T> filter(List<T> items) {

        List<T> itemsToFilter = new ArrayList<>(items);

        if (filterValueExtractor == null || filterValues.isEmpty())
            return itemsToFilter;

        itemsToFilter = itemsToFilter.stream()
                .filter(t -> (filterValueExtractor.apply(t).equals(selectedFilter.getValue())))
                .collect(Collectors.toList());

        return itemsToFilter;
    }

    private void registerButtonListener() {

        nextPageButton.setOnClickListener((View view) -> {

            if (currentPage.getValue() == null)
                throw new IllegalStateException("Current page field should never be null!");

            if (itemsSortedAndFiltered.getValue() == null)
                return;

            if (currentPage.getValue() == MediaUtil.getTotalNumberOfPages(itemsSortedAndFiltered.getValue(), ELEMENTS_PER_PAGE))
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

        sortButton.setOnClickListener((View view) -> toggleSorting());

        itemFilterSpinner.setOnItemSelectedListener(new ItemFilterSelectedListener());
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

            View view = itemToViewConverter == null ? createDefaultTextView(item.toString()) : itemToViewConverter.apply(item);

            itemList.addView(view);
        }
    }

    private TextView createDefaultTextView(String text) {

        TextView textView = (TextView) View.inflate(getContext(), R.layout.list_element, null);
        textView.setText(text);

        return textView;
    }

    public void setItemSortingStringConverter(Function<T, String> itemSortingStringConverter) {
        this.itemSortingStringConverter = itemSortingStringConverter;
    }

    public void setUpFilter(List<String> filterValues, Function<T, String> filterValueExtractor) {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.media_type_spinner_item, filterValues);

        arrayAdapter.setDropDownViewResource(R.layout.media_type_spinner_dropdown);
        itemFilterSpinner.setAdapter(arrayAdapter);

        this.filterValues = filterValues;
        this.filterValueExtractor = filterValueExtractor;
    }

    public void setItemToViewConverter(Function<T, View> itemToViewConverter) {
        this.itemToViewConverter = itemToViewConverter;
    }

    private class PageNumberCurrentObserver implements Observer<Integer> {

        @Override
        public void onChanged(Integer pageNumber) {

            currentPageLabel.setText(NumberUtil.formatNumberFullDigitsLeadingZero(pageNumber));
            showMediaOnCurrentPage(items.getValue());
        }
    }

    private class ItemComparator implements Comparator<T> {

        @Override
        public int compare(T item1, T item2) {

            String stringItem1 = itemSortingStringConverter == null ? item1.toString() : itemSortingStringConverter.apply(item1);
            String stringItem2 = itemSortingStringConverter == null ? item2.toString() : itemSortingStringConverter.apply(item2);

            return stringItem1.compareTo(stringItem2);
        }
    }

    private class ItemFilterSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            if (i == 0)
                selectedFilter.setValue(null);
            else
                selectedFilter.setValue(filterValues.get(i));

            currentPage.setValue(1);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            /* TODO: When is this triggered? */
        }
    }

    private class PageNumberTotalObserver implements Observer<List<T>> {

        @Override
        public void onChanged(List<T> mediaList) {

            int numberOfPages = MediaUtil.getTotalNumberOfPages(mediaList, ELEMENTS_PER_PAGE);

            maxPageLabel.setText(NumberUtil.formatNumberFullDigitsLeadingZero(numberOfPages));
        }
    }
}