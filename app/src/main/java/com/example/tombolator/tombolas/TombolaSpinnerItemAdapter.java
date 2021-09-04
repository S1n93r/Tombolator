package com.example.tombolator.tombolas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tombolator.R;

import java.util.List;

public class TombolaSpinnerItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<Tombola> tombolaList;

    private final LayoutInflater inflater;

    public TombolaSpinnerItemAdapter(Context context, List<Tombola> tombolaList) {

        this.context = context;
        this.tombolaList = tombolaList;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tombolaList.size();
    }

    @Override
    public Object getItem(int i) {
        return tombolaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = inflater.inflate(R.layout.tombola_spinner_item, viewGroup, false);

        TextView subTextView = rowView.findViewById(R.id.text_view_content);

        subTextView.setText(tombolaList.get(i).getName());

        return rowView;
    }
}