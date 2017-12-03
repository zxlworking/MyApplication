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

    private ImageView mTitleBarLeftImg;
    private ImageView mTitleBarRightImg;

    public View.OnClickListener mOnClickListener = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        ActivityManager.getInstance().addActivity(this);

        mContext = this;
        mActivity = this;

        initView(savedInstanceState);
        initData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    public abstract int getContentView();

    public void initView(Bundle savedInstanceState){
        initView();

    }

    public void initView(){
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mTitleBarLeftImg = (ImageView) findViewById(R.id.title_bar_left_img);
        mTitleBarRightImg = (ImageView) findViewById(R.id.title_bar_right_img);

    }

    public void initData(){
        if(mTitleBarLeftImg != null){
            mTitleBarLeftImg.setOnClickListener(mOnClickListener);
        }
        if(mTitleBarRightImg != null){
            mTitleBarRightImg.setOnClickListener(mOnClickListener);
        }
    }

    public void setTitle(String title){
        if(mTitleTv != null){
            mTitleTv.setText(title);
        }
    }

    public void setLeftImgVisibility(int v){
        if(mTitleBarLeftImg != null){
            mTitleBarLeftImg.setVisibility(v);
        }
    }

    public void setRightImgVisibility(int v){
        if(mTitleBarRightImg != null){
            mTitleBarRightImg.setVisibility(v);
        }
    }

    public void setRightImgRes(int resId){
        if(mTitleBarRightImg != null){
            mTitleBarRightImg.setVisibility(View.VISIBLE);
            mTitleBarRightImg.setImageResource(resId);
        }
    }
}
