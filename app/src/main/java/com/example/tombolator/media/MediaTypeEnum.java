package com.example.tombolator.media;

import java.util.Map;

public enum MediaTypeEnum {

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

    MediaTypeEnum(String cleanName) {
        this.cleanName = cleanName;
    }

    /* TODO: Remove as soon as Susi has newest version. */
    @Deprecated
    public static MediaTypeEnum fromOldString(String string) {

        Map<String, String> matcher = MediaTypEnumHelper.getMatcher();

        if (matcher.containsKey(string))
            return MediaTypeEnum.valueOf(matcher.get(string));

        return MediaTypeEnum.valueOf(string);
    }

    public String getCleanName() {
        return cleanName;
    }
}