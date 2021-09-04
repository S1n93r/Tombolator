package com.example.tombolator;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.text.MessageFormat;

public final class ToasterUtil {

    private ToasterUtil() {
        /* Util class */
    }

    public static void makeShortToast(Activity activity, Context context, String message) {

        activity.runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }

    public static void makeShortToast(Activity activity, Context context, String message, Object... arguments) {

        String out = MessageFormat.format(message, arguments);

        activity.runOnUiThread(() -> Toast.makeText(context, out, Toast.LENGTH_SHORT).show());
    }

    public static void makeLongToast(Activity activity, Context context, String message, Object... arguments) {

        String out = MessageFormat.format(message, arguments);

        activity.runOnUiThread(() -> Toast.makeText(context, out, Toast.LENGTH_LONG).show());
    }
}
