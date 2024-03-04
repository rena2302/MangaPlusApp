package com.example.mangaplusapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityUtils {
    public static void startNewActivity(Context context, Class<?> newActivityClass, String... extras) {
        Intent intent = new Intent(context, newActivityClass);
        if (extras.length % 2 == 0) {
            for (int i = 0; i < extras.length; i += 2) {
                intent.putExtra(extras[i], extras[i + 1]);
            }
        }
        context.startActivity(intent);
    }
    public static void startNewActivityAndFinishCurrent(Context context, Class<?> newActivityClass, String... extras) {
        Intent intent = new Intent(context, newActivityClass);
        if (extras.length % 2 == 0) {
            for (int i = 0; i < extras.length; i += 2) {
                intent.putExtra(extras[i], extras[i + 1]);
            }
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }

}

