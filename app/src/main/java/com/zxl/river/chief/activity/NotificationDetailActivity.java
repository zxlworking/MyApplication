package com.zxl.river.chief.activity;

import android.view.View;

import com.zxl.river.chief.R;

/**
 * Created by mac on 17-12-2.
 */

public class NotificationDetailActivity extends BaseActivity {
    @Override
    public int getContentView() {
        return R.layout.notification_detail_activity;
    }

    @Override
    public void initView() {
        super.initView();

        setTitle("公告详情");
        setRightImgVisibility(View.GONE);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.title_bar_left_img:
                        finish();
                        break;
                }
            }
        };

    }

    @Override
    public void initData() {
        super.initData();
    }
}
