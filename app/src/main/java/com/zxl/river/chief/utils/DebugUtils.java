package com.zxl.river.chief.utils;

import android.util.Log;

/**
 * Created by mac on 17-11-21.
 */

public class DebugUtils {
    private static final boolean isDebug = true;

    public void d(String tag,String msg){
        if(isDebug){
            Log.d(tag,msg);
        }
    }
}
