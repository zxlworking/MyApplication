package com.zxl.river.chief.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.List;


/**
 * Created by uidq0955 on 2017/9/25.
 */

public class CommonUtils {
    private static final String TAG = "CommonUtils";

    public static void showMessage(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static void showMessage(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }
}
