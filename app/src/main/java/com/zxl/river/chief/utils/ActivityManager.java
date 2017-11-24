package com.zxl.river.chief.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.preference.Preference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-11-24.
 */

public class ActivityManager {

    private static ActivityManager mActivityManager;

    private List<Activity> mActivities = new ArrayList<>();

    public static ActivityManager getInstance(){
        if(null == mActivityManager){
            mActivityManager = new ActivityManager();
        }
        return mActivityManager;
    }

    private ActivityManager(){
    }

    public void addActivity(Activity activity){
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity){
        mActivities.remove(activity);
    }

    public void finishAllActivity(){
        for(Activity activity : mActivities){
            activity.finish();
        }
    }
}
