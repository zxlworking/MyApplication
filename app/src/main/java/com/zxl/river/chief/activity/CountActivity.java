package com.zxl.river.chief.activity;

import android.app.FragmentTransaction;

import com.zxl.river.chief.R;
import com.zxl.river.chief.fragmetn.CountFragment;

/**
 * Created by mac on 17-12-3.
 */

public class CountActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.count_activity;
    }

    @Override
    public void initView() {
        super.initView();
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.count_content_fl,new CountFragment());
        mFragmentTransaction.commit();
    }

    @Override
    public void initData() {
        super.initData();
    }
}
