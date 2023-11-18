package com.example.tombolator.media;

import com.example.tombolator.R;

import java.util.List;

public final class MediaUtil {

    public static int getMediaTypeIcon(MediaType mediaType) {

        switch (mediaType) {

            case CD:
            case DVD:
            case BLU_RAY:
                return R.drawable.ic_cd;

            case BOOK:
                return R.drawable.ic_book;

            case E_BOOK:
                return R.drawable.ic_ebook;

            case STREAMING:
                return R.drawable.ic_online_streaming_25;

            case MEAL:
                return R.drawable.ic_meal;

            case CASSETTE:
            default:
                return R.drawable.ic_cassette;
        }
    }

    public static int getMediaTypeIcon(Media media) {
        return getMediaTypeIcon(media.getMediaType());
    }

    public static int getTotalNumberOfPages(List<?> list, int elementsPerPage) {
        return (int) (Math.ceil((double) list.size() / elementsPerPage));
    }
}