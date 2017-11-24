package com.zxl.river.chief.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zxl.river.chief.utils.ActivityManager;

/**
 * Created by mac on 17-11-24.
 */

public abstract class BaseActivity extends Activity {

    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        ActivityManager.getInstance().addActivity(this);

        mContext = this;
        initView();
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    public abstract int getContentView();
    public abstract void initView();
    public abstract void initData();
}
