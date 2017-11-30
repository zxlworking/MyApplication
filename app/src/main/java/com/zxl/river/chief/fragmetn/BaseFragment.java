package com.zxl.river.chief.fragmetn;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxl.river.chief.R;

/**
 * Created by mac on 17-11-20.
 */

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public View mContentView;

    private TextView mTitleTv;

    private ImageView mBackImg;
    private ImageView mSettingsImg;

    public View.OnClickListener mOnClickListener = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = LayoutInflater.from(mContext).inflate(getContentView(),null);

        initView(mContentView,savedInstanceState);
        initData();

        return mContentView;
    }

    public abstract int getContentView();

    public void initView(View contentView,Bundle savedInstanceState){
        mTitleTv = (TextView) contentView.findViewById(R.id.title_tv);
        mBackImg = (ImageView) contentView.findViewById(R.id.back_img);
        mSettingsImg = (ImageView) contentView.findViewById(R.id.settings_img);

    }
    public void initData(){
        if(mOnClickListener != null){
            if(mBackImg != null){
                mBackImg.setOnClickListener(mOnClickListener);
            }
            if(mSettingsImg != null){
                mSettingsImg.setOnClickListener(mOnClickListener);
            }
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
