package com.example.tombolator;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private DateUtil() {
        //Util classes have private access.
    }

    public static String formatDate(long dateInMillis) {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(dateInMillis));
    }
}