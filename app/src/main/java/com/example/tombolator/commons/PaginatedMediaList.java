package com.example.tombolator.commons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import lombok.Setter;

import java.util.List;

public class PaginatedMediaList extends ConstraintLayout {

    private static final String FILTER_ALL_CATEGORIES = "[show all categories]";

    private final MutableLiveData<Integer> currentPage = new MutableLiveData<>(1);

    private final MutableLiveData<String> selectedMediaType = new MutableLiveData<>(FILTER_ALL_CATEGORIES);

    private final ImageView sortButton;
    private final Spinner mediaTypesSpinner;
    private final LinearLayout linearLayoutMedia;

    private final Button nextPageButton;
    private final TextView pageNumberCurrent;
    private final TextView pageNumberMax;
    private final Button previousPageButton;

    private final Button backButton;
    private final Button newMediaButton;

    @Setter
    private View.OnClickListener showDetailsListener;

    @Setter
    private int elementsPerPage;

    public PaginatedMediaList(@NonNull Context context) {

        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.paginated_media_list, this, true);

        linearLayoutMedia = findViewById(R.id.linear_layout_media);

        mediaTypesSpinner = findViewById(R.id.spinner_media_types);

        pageNumberCurrent = findViewById(R.id.label_page_number_current);
        pageNumberMax = findViewById(R.id.label_page_number_total);

        sortButton = findViewById(R.id.button_sort_by);
        backButton = findViewById(R.id.back_button);
        nextPageButton = findViewById(R.id.button_next_page);
        previousPageButton = findViewById(R.id.button_previous_page);
        newMediaButton = findViewById(R.id.button_new_media);

        setUpMediaTypesSpinner();
    }

    public PaginatedMediaList(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.paginated_media_list, this, true);

        linearLayoutMedia = view.findViewById(R.id.linear_layout_media);

        mediaTypesSpinner = view.findViewById(R.id.spinner_media_types);

        pageNumberCurrent = view.findViewById(R.id.label_page_number_current);
        pageNumberMax = view.findViewById(R.id.label_page_number_total);

        sortButton = view.findViewById(R.id.button_sort_by);
        backButton = view.findViewById(R.id.back_button);
        nextPageButton = view.findViewById(R.id.button_next_page);
        previousPageButton = view.findViewById(R.id.button_previous_page);
        newMediaButton = view.findViewById(R.id.button_new_media);

        setUpMediaTypesSpinner();
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