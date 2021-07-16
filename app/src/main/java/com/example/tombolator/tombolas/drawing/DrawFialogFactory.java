package com.example.tombolator.tombolas.drawing;

import android.content.Context;

import java.util.Random;

public final class DrawFialogFactory {

    public final static int DIALOG_TV = 0;
    public final static int DIALOG_SUNFLOWER = 1;
    public final static int DIALOG_LIGHTBULB = 2;
    public final static int DIALOG_COIN = 3;

    public static DrawDialog createDialog(Context context, int type) {

        switch(type) {

            case DIALOG_TV:
            default: return new DrawDialogTv(context);

            case DIALOG_SUNFLOWER: return new DrawDialogSunflower(context);
            case DIALOG_LIGHTBULB: return new DrawDialogLightBulb(context);
            case DIALOG_COIN: return new DrawDialogCoin(context);
        }
    }

    public static DrawDialog createRandomDialog(Context context) {

        Random random = new Random();
        int type = random.nextInt(4);

        return createDialog(context, type);
    }
}