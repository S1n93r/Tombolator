package com.example.tombolator.tombolas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tombolator.R;
import com.example.tombolator.media.Media;
import com.example.tombolator.media.MediaType;

public class ChooseMediaTypeFragment extends Fragment {

    private TombolasActivity tombolasActivity;
    private TombolasActivityViewModel viewModel;

    private ImageView cassetteButton;
    private ImageView cdButton;
    private ImageView bookButton;
    private ImageView ebookButton;
    private ImageView streamingButton;
    private ImageView movieButton;

    private ImageView backButton;

    private ChooseMediaTypeFragment() {
    }

    public static ChooseMediaTypeFragment newInstance() {
        return new ChooseMediaTypeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tombolasActivity = (TombolasActivity) getActivity();
        viewModel = new ViewModelProvider(requireActivity()).get(TombolasActivityViewModel.class);

        View layout = inflater.inflate(R.layout.choose_media_type_fragment, container, false);

        cdButton = layout.findViewById(R.id.cd_button);
        bookButton = layout.findViewById(R.id.book_button);
        ebookButton = layout.findViewById(R.id.ebook_button);
        movieButton = layout.findViewById(R.id.movie_button);
        streamingButton = layout.findViewById(R.id.streaming_button);
        cassetteButton = layout.findViewById(R.id.cassette_button);

        backButton = layout.findViewById(R.id.back_button);

        registerOnClickListener();

        return layout;
    }

    private void registerOnClickListener() {

        cdButton.setOnClickListener((View v) -> chooseAndSwitch(MediaType.CD, () -> tombolasActivity.switchToCreateCassette()));
        cassetteButton.setOnClickListener((View v) -> chooseAndSwitch(MediaType.CASSETTE, () -> tombolasActivity.switchToCreateCassette()));

        movieButton.setOnClickListener((View v) -> chooseAndSwitch(MediaType.DVD, () -> tombolasActivity.switchToCreateMovie()));
        streamingButton.setOnClickListener((View v) -> chooseAndSwitch(MediaType.STREAMING, () -> tombolasActivity.switchToCreateMovie()));

        bookButton.setOnClickListener((View v) -> chooseAndSwitch(MediaType.BOOK, () -> tombolasActivity.switchToCreateBook()));
        ebookButton.setOnClickListener((View v) -> chooseAndSwitch(MediaType.E_BOOK, () -> tombolasActivity.switchToCreateBook()));

        backButton.setOnClickListener((View v) -> tombolasActivity.switchToCreateTombola());
    }

    private void chooseAndSwitch(MediaType mediaType, Runnable switchRunner) {

        Media selectedMedia = viewModel.getSelectedMedia().getValue();

        if (selectedMedia == null)
            throw new IllegalStateException("Media should not be 'null' here. Check " + CreateTombolaFragment.class.getName() + ".");

        selectedMedia.setMediaType(mediaType);

        switchRunner.run();
    }
}