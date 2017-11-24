package com.zxl.river.chief.fragmetn;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mac on 17-11-20.
 */

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public View mContentView;

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
    public abstract void initView(View contentView,Bundle savedInstanceState);
    public abstract void initData();

}
