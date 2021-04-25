package com.example.tombolator.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.example.tombolator.R;

import java.util.List;
import java.util.Objects;

public class FilterablePaginatedList<T> extends Fragment {

    private final View.OnClickListener listenerForListElements;
    private final Function<T, String> textExtractor;
    private final Function<T, Integer> idExtractor;
    private final Function<T, Integer> iconExtractor;

    private LinearLayout elementsLinearLayout;

    public FilterablePaginatedList(LiveData<List<T>> observableElements, View.OnClickListener listenerForListElements,
                                   Function<T, String> textExtractor, Function<T, Integer> idExtractor,
                                   Function<T, Integer> iconExtractor) {

        this.listenerForListElements = listenerForListElements;
        this.textExtractor = textExtractor;
        this.idExtractor = idExtractor;
        this.iconExtractor = iconExtractor;

        LifecycleOwner owner = (LifecycleOwner) Objects.requireNonNull(getActivity()).getApplicationContext();
        observableElements.observe(owner, new ElementListObserver());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.filterable_paginated_list, container, false);

        elementsLinearLayout = layout.findViewById(R.id.linear_layout_elements);

        return layout;
    }

    private class ElementListObserver implements Observer<List<T>> {

        @Override
        public void onChanged(List<T> elementList) {

            elementsLinearLayout.removeAllViews();

            for(T element : elementList) {

                String text = textExtractor.apply(element);
                int id = idExtractor.apply(element);
                int iconId = iconExtractor.apply(element);

                TextView textView = (TextView) View.inflate(
                        Objects.requireNonNull(
                                getActivity()).getApplicationContext(), R.layout.list_element, null);

                textView.setOnClickListener(listenerForListElements);
                textView.setText(text);
                textView.setId(id);

                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        iconId, 0, 0, 0);

                elementsLinearLayout.addView(textView);
            }
        }
    }
}