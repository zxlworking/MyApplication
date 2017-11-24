package com.zxl.river.chief.fragmetn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zxl.river.chief.R;
import com.zxl.river.chief.activity.EventListActivity;

/**
 * Created by mac on 17-11-21.
 */

public class MeFragment extends BaseFragment {

    private LinearLayout mDealEventLl;

    @Override
    public int getContentView() {
        return R.layout.me_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        mDealEventLl = (LinearLayout) contentView.findViewById(R.id.deal_event_ll);

        mDealEventLl.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.deal_event_ll:
                    Intent mSettingsIntent = new Intent(mContext,EventListActivity.class);
                    mSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mSettingsIntent);
                    break;
            }
        }
    };
}
