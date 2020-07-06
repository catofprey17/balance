package ru.c17.balance.Utils;

import android.graphics.Color;

import androidx.annotation.ColorInt;

public class ColorUtils {

    @ColorInt
    public static int getLighter(@ColorInt int color, float change) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= change;
        return Color.HSVToColor(hsv);
    }

    public static int getLighter(int color) {
        return getLighter(color, 1.5f);
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}

