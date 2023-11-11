package com.example.tombolator.media;

import java.util.Map;

public enum MediaType {

    ALL("Alle"),
    CASSETTE("Kassette"),
    CD("CD"),
    DVD("DVD"),
    BLU_RAY("Blu-ray"),
    E_BOOK("E-Book"),
    BOOK("Buch"),
    STREAMING("Streaming"),
    MEAL("Essen");

    private final String cleanName;

    MediaType(String cleanName) {
        this.cleanName = cleanName;
    }

    /* TODO: Remove as soon as Susi has newest version. */
    @Deprecated
    public static MediaType fromOldString(String string) {

        Map<String, String> matcher = EnumHelper.getMediaTypeMatcher();

        if (matcher.containsKey(string))
            return MediaType.valueOf(matcher.get(string));

        return MediaType.valueOf(string);
    }

    public String getCleanName() {
        return cleanName;
    }
}