package com.example.tombolator.tombolas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.tombolator.R;
import com.example.tombolator.TomboDbApplication;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaActivityViewModel;

import java.util.Objects;

public class TombolaCreateNewFragment extends Fragment {

    TombolasActivity parent;

    public static TombolaCreateNewFragment newInstance(TombolasActivity parent) {
        return new TombolaCreateNewFragment(parent);
    }

    private TombolaCreateNewFragment(TombolasActivity parent) {
        this.parent = parent;
    }

    private MediaActivityViewModel mediaActivityViewModel;

    private TextView editTextName;

    private LinearLayout availableMedia;
    private LinearLayout addedMedia;

    private Button saveButton;
    private Button backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mediaActivityViewModel = new ViewModelProvider(requireActivity()).get(MediaActivityViewModel.class);

        View layout = inflater.inflate(R.layout.fragment_tombolas_new, container, false);

        editTextName = layout.findViewById(R.id.edit_text_name);

        availableMedia = layout.findViewById(R.id.linear_layout_available_media);
        addedMedia = layout.findViewById(R.id.linear_layout_added_media);

        saveButton = layout.findViewById(R.id.button_save);
        backButton = layout.findViewById(R.id.button_back);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editTextName.getText().length() > 0){

                    String name = editTextName.getText() != null ? editTextName.getText().toString() : "";

                    final Tombola tombola = new Tombola(name);

                    if(addedMedia.getChildCount() > 0) {

                        for(int i=0; i<addedMedia.getChildCount(); i++) {

                            TextView textView = (TextView) addedMedia.getChildAt(i);
                            long mediaId = textView.getId();

                            if(mediaActivityViewModel.getMediaDatabase().getValue() == null)
                                throw new NullPointerException();

                            Media media = mediaActivityViewModel.getMediaDatabase().getValue().get(mediaId);

                            tombola.addMedia(media);
                        }
                    }

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            TomboDbApplication context = ((TomboDbApplication) Objects.requireNonNull(getActivity())
                                    .getApplicationContext());

                            final TombolaDao tombolaDao = context.getTomboDb().tombolaDao();
                            tombolaDao.insertTombola(tombola);
                        }
                    });

                    resetForm();

                    parent.switchToMainView();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.switchToMainView();
            }
        });
    }

    private void resetForm() {
        editTextName.setText("");
        availableMedia.removeAllViews();
        addedMedia.removeAllViews();
    }
}