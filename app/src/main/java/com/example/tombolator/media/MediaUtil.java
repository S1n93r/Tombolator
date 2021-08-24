package com.example.tombolator.media;

import com.example.tombolator.R;

import java.util.List;

public final class MediaUtil {

    public static int getMediaTypeIcon(String mediaType) {

        switch(mediaType) {

            case Media.MediaType.CD:
            case Media.MediaType.DVD:
            case Media.MediaType.BLU_RAY:
                return R.drawable.ic_cd_25;

            case Media.MediaType.BOOK:
                return R.drawable.ic_book_25;

            case Media.MediaType.E_BOOK:
                return R.drawable.ic_ebook_25;

            case Media.MediaType.STREAMING:
                return R.drawable.ic_online_streaming_25;

            case Media.MediaType.MEAL:
                return R.drawable.ic_meal_25;

            case Media.MediaType.CASSETTE:
            default:
                return R.drawable.ic_cassette_25;
        }
    }

    public static int getMediaTypeIcon(Media media) {
        return getMediaTypeIcon(media.getMediaType());
    }

    public static int getTotalNumberOfPages(List<?> list, int elementsPerPage) {
        return (int) (Math.ceil((double) list.size() / elementsPerPage));
    }
}