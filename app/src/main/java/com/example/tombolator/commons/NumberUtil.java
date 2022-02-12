package com.example.tombolator.commons;

import java.util.Locale;

public class NumberUtil {

    public static String formatNumberFullDigitsLeadingZero(int number) {

        int numberOfDigits = String.valueOf(number).length();

        String numberFormat = "%0" + numberOfDigits + "d";

        return String.format(Locale.getDefault(), numberFormat, number);
    }
}