package com.example.tombolator.media;

import com.example.tombolator.R;

import java.util.Collection;

public final class MediaUtil {

    public static int getMediaIcon(Media media) {

        String type = media.getType();

        switch(type) {

            case Media.Type.CD:
            case Media.Type.DVD:
            case Media.Type.BLU_RAY:
                return R.drawable.ic_cd_25;

            case Media.Type.BOOK:
                return R.drawable.ic_book_25;

            case Media.Type.E_BOOK:
                return R.drawable.ic_ebook_25;

            case Media.Type.MOVIE:
                return R.drawable.ic_film_25;

            case Media.Type.CASSETTE:
            default:
                return R.drawable.ic_cassette_25;
        }
    }

    public static boolean listContainsMedia(Collection<Media> mediaCollection, long id) {

        for(Media media : mediaCollection) {
            if(media.getId() == id) {
                return true;
            }

        }

        return false;
    }
}
