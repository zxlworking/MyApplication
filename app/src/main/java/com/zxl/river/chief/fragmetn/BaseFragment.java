package com.zxl.river.chief.fragmetn;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxl.river.chief.R;

/**
 * Created by mac on 17-11-20.
 */

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public View mContentView;

    private LinearLayout mTitleBarLl;

    private TextView mTitleTv;

    private ImageView mTitleBarLeftImg;
    private ImageView mTitleBarRightImg;

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
        mTitleBarLl = (LinearLayout) contentView.findViewById(R.id.title_bar_ll);
        mTitleTv = (TextView) contentView.findViewById(R.id.title_tv);
        mTitleBarLeftImg = (ImageView) contentView.findViewById(R.id.title_bar_left_img);
        mTitleBarRightImg = (ImageView) contentView.findViewById(R.id.title_bar_right_img);

    }
    public void initData(){
        if(mOnClickListener != null){
            if(mTitleBarLeftImg != null){
                mTitleBarLeftImg.setOnClickListener(mOnClickListener);
            }
            if(mTitleBarRightImg != null){
                mTitleBarRightImg.setOnClickListener(mOnClickListener);
            }
        }
    }

    public void setTitle(String title){
        if(mTitleTv != null){
            mTitleTv.setText(title);
        }
    }

    public void setTitleBarVisibility(int v){
        if(mTitleBarLl != null){
            mTitleBarLl.setVisibility(v);
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

}
