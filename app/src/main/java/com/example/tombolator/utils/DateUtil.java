package com.example.tombolator.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {

    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_DATE = "dd.MM.yyyy";
    private static final String FORMAT_TIME_AND_DATE = FORMAT_DATE + " " + FORMAT_TIME;

    private DateUtil() {
        //Util classes have private access.
    }

    public static String formatTime(long dateInMillis) {
        return new SimpleDateFormat(FORMAT_TIME).format(new Date(dateInMillis));
    }

    public static String formatDate(long dateInMillis) {
        return new SimpleDateFormat(FORMAT_DATE).format(new Date(dateInMillis));
    }

    public static String formatDateAndTime(long dateInMillis) {
        return new SimpleDateFormat(FORMAT_TIME_AND_DATE).format(new Date(dateInMillis));
    }
}