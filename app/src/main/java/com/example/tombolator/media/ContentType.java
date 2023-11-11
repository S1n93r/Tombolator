package com.example.tombolator.media;

import java.util.Map;

public enum ContentType {

    AUDIO_PLAY("Hörspiel"),
    MOVIE("Film"),
    SERIES("Serie");

    private final String cleanName;

    ContentType(String cleanName) {
        this.cleanName = cleanName;
    }

    /* TODO: Remove as soon as Susi has newest version. */
    @Deprecated
    public static ContentType fromOldString(String string) {

        Map<String, String> matcher = EnumHelper.getContentTypeMatcher();

        if (matcher.containsKey(string))
            return ContentType.valueOf(matcher.get(string));

        return ContentType.valueOf(string);
    }

    public String getCleanName() {
        return cleanName;
    }
}