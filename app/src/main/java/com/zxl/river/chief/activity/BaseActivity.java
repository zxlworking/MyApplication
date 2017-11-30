package com.zxl.river.chief.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxl.river.chief.R;
import com.zxl.river.chief.utils.ActivityManager;

/**
 * Created by mac on 17-11-24.
 */

public abstract class BaseActivity extends Activity {

    public Context mContext;

    public Activity mActivity;

    private TextView mTitleTv;

    private ImageView mBackImg;
    private ImageView mSettingsImg;

    public View.OnClickListener mOnClickListener = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        ActivityManager.getInstance().addActivity(this);

        mContext = this;
        mActivity = this;

        initView();
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    public abstract int getContentView();

    public void initView(){
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mBackImg = (ImageView) findViewById(R.id.back_img);
        mSettingsImg = (ImageView) findViewById(R.id.settings_img);

    }
    public void initData(){
        if(mBackImg != null){
            mBackImg.setOnClickListener(mOnClickListener);
        }
        if(mSettingsImg != null){
            mSettingsImg.setOnClickListener(mOnClickListener);
        }
    }

    public void setTitle(String title){
        if(mTitleTv != null){
            mTitleTv.setText(title);
        }
    }

    public void setBackImgVisibility(int v){
        if(mBackImg != null){
            mBackImg.setVisibility(v);
        }
    }

    public void setSettingsImgVisibility(int v){
        if(mSettingsImg != null){
            mSettingsImg.setVisibility(v);
        }
    }
}
