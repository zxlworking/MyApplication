package com.zxl.river.chief.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.WindowManager;
import android.widget.Toast;

import com.zxl.river.chief.common.Constants;

import java.io.File;


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

    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        Point mPoint = new Point();
        wm.getDefaultDisplay().getSize(mPoint);
        return mPoint.x;
    }

    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        Point mPoint = new Point();
        wm.getDefaultDisplay().getSize(mPoint);
        return mPoint.y;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean checkStoragePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PhotoUtils.STORAGE_PERMISSIONS_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }

    public static Uri fileToUri(Context context, File file){
        Uri mUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mUri = FileProvider.getUriForFile(context, Constants.FILEPROVIDER_AUTHORITIES, file);//通过FileProvider创建一个content类型的Uri
        }else{
            mUri = Uri.fromFile(file);
        }
        return mUri;
    }

    public static Uri fileToUri(Activity activity,File file){
        Uri mUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mUri = FileProvider.getUriForFile(activity, Constants.FILEPROVIDER_AUTHORITIES, file);//通过FileProvider创建一个content类型的Uri
        }else{
            mUri = Uri.fromFile(file);
        }
        return mUri;
    }
}
