package com.zxl.river.chief.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.zxl.river.chief.common.Constants;

/**
 * Created by mac on 17-11-24.
 */

public class Preference {

    private static Preference mPreference;
    private SharedPreferences mSharedPreferences;

    public static Preference getInstance(Context context){
        if(null == mPreference){
            mPreference = new Preference(context);
        }
        return mPreference;
    }

    private Preference(Context context){
        mSharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME,0);
    }

    public void setRememberMe(boolean b){
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("RememberMe",b);
        mEditor.commit();
    }

    public boolean getRememberMe(){
        return mSharedPreferences.getBoolean("RememberMe",false);
    }

    public void setUserName(String userName){
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("UserName",userName);
        mEditor.commit();
    }

    public String getUserName(){
        return mSharedPreferences.getString("UserName","");
    }

    public void setPassWord(String passWord){
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString("PassWord",passWord);
        mEditor.commit();
    }

    public String getPassWord(){
        return mSharedPreferences.getString("PassWord","");
    }

    public void setRiverRate(long time){
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putLong("RiverRate",time);
        mEditor.commit();
    }

    public long getRiverRate(){
        return mSharedPreferences.getLong("RiverRate",60000);
    }

}
