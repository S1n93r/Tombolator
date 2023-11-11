package com.example.tombolator.media;

import java.util.HashMap;
import java.util.Map;

/* TODO: Remove as soon as Susi has newest version. */
@Deprecated
public final class EnumHelper {

    private EnumHelper() {
        /* Helper */
    }

    /* TODO: Remove as soon as Susi has newest version. */
    @Deprecated
    public static Map<String, String> getMediaTypeMatcher() {

        Map<String, String> matcherMap = new HashMap<>();

        for (MediaType value : MediaType.values())
            matcherMap.put(value.getCleanName(), value.name());

        return matcherMap;
    }

    /* TODO: Remove as soon as Susi has newest version. */
    @Deprecated
    public static Map<String, String> getContentTypeMatcher() {

        Map<String, String> matcherMap = new HashMap<>();

        for (ContentType value : ContentType.values())
            matcherMap.put(value.getCleanName(), value.name());

        return matcherMap;
    }
}