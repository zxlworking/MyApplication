package com.zxl.river.chief.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxl.river.chief.R;
import com.zxl.river.chief.preference.Preference;
import com.zxl.river.chief.utils.ActivityManager;

/**
 * Created by mac on 17-11-24.
 */

public class SettingsActivity extends BaseActivity {

    private LinearLayout mLoginOutLl;

    @Override
    public int getContentView() {
        return R.layout.settings_activity;
    }

    @Override
    public void initView() {
        super.initView();
        mLoginOutLl = (LinearLayout) findViewById(R.id.login_out_ll);

        setSettingsImgVisibility(View.GONE);
        setTitle("设置");

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.back_img:
                        finish();
                        break;
                    case R.id.login_out_ll:
                        Preference.getInstance(mContext).setRememberMe(false);
                        Preference.getInstance(mContext).setUserName("");
                        Preference.getInstance(mContext).setPassWord("");

                        Intent mSettingsIntent = new Intent(mContext,LoginActivity.class);
                        mSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mSettingsIntent);

                        ActivityManager.getInstance().finishAllActivity();
                        break;
                }
            }
        };
        mLoginOutLl.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {
        super.initData();
    }


}
