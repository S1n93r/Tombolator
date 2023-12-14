package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.commons.PaginatedListComponent;

public class TombolaMainFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel tombolasActivityViewModel;
    private PaginatedListComponent<Tombola> paginatedTombolaList;
    private ImageView backButton;
    private ImageView newTombolaButton;

    private TombolaMainFragment() {

    }

    public static TombolaMainFragment newInstance() {
        return new TombolaMainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        tombolasActivityViewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.tombola_list_fragment, container, false);

        paginatedTombolaList = layout.findViewById(R.id.paginated_tombola_list);

        backButton = layout.findViewById(R.id.back_button);
        newTombolaButton = layout.findViewById(R.id.button_new_tombola);

        configurePaginatedTombolaList();
        registerOnClickListener();
        registerObserver();

        return layout;
    }

    private void configurePaginatedTombolaList() {

        paginatedTombolaList.setItemSortingStringConverter(Tombola::getName);

        paginatedTombolaList.setItemToViewConverter(tombola -> {

            TextView textView = (TextView) View.inflate(
                    tombolasActivity.getApplicationContext(), R.layout.list_element, null);

            textView.setText(tombola.toLabel());
            textView.setOnClickListener(new ShowDetailsListener());
            textView.setId(tombola.getId().intValue());

            return textView;
        });
    }

    private void registerObserver() {
        tombolasActivityViewModel.getAllTombolas().observe(getViewLifecycleOwner(), tombolas -> {
            if (tombolasActivityViewModel.getAllTombolas() != null)
                paginatedTombolaList.setItems(getViewLifecycleOwner(), tombolasActivityViewModel.getAllTombolas());
        });
    }

    private void registerOnClickListener() {

        backButton.setOnClickListener(view -> tombolasActivity.finish());
        newTombolaButton.setOnClickListener(view -> {

            tombolasActivityViewModel.selectTombola(new Tombola());
            tombolasActivity.switchToCreateTombolaView();
        });
    }

    private class ShowDetailsListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            TextView textView = (TextView) view;
            long tombolaId = textView.getId();
            tombolasActivityViewModel.selectTombola(tombolaId);

            tombolasActivity.switchToTombolaDetailsView();
        }
    }
}