package com.example.tombolator.media;

import java.util.HashMap;
import java.util.Map;

/* TODO: Remove as soon as Susi has newest version. */
@Deprecated
public final class MediaTypEnumHelper {

    private MediaTypEnumHelper() {
        /* Helper */
    }

    /* TODO: Remove as soon as Susi has newest version. */
    @Deprecated
    public static Map<String, String> getMatcher() {

        Map<String, String> matcherMap = new HashMap<>();

        for (MediaTypeEnum value : MediaTypeEnum.values()) {
            matcherMap.put(value.getCleanName(), value.name());
        }

        return matcherMap;
    }
}