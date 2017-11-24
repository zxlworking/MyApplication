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

    private ImageView mBackImg;
    private ImageView mSettingsImg;

    private TextView mTitleTv;

    private LinearLayout mLoginOutLl;

    @Override
    public int getContentView() {
        return R.layout.settings_activity;
    }

    @Override
    public void initView() {

        mBackImg = (ImageView) findViewById(R.id.back_img);
        mSettingsImg = (ImageView) findViewById(R.id.settings_img);

        mTitleTv = (TextView) findViewById(R.id.title_tv);

        mLoginOutLl = (LinearLayout) findViewById(R.id.login_out_ll);

        mSettingsImg.setVisibility(View.GONE);
        mTitleTv.setText("设置");

        mBackImg.setOnClickListener(mOnClickListener);
        mLoginOutLl.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
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
}
