package com.zxl.river.chief.fragmetn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zxl.river.chief.R;
import com.zxl.river.chief.activity.LoginActivity;
import com.zxl.river.chief.preference.Preference;
import com.zxl.river.chief.utils.ActivityManager;

/**
 * Created by mac on 17-12-2.
 */

public class SettingsFragment extends BaseFragment {

    private static final int[] RIVER_RATE_ARRAY = new int[]{1,2,3,4,5,10};

    private LinearLayout mLoginOutLl;

    private Spinner mRiverRateSpinner;


    @Override
    public int getContentView() {
        return R.layout.settings_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        super.initView(contentView,savedInstanceState);
        mLoginOutLl = (LinearLayout) contentView.findViewById(R.id.login_out_ll);

        mRiverRateSpinner = (Spinner) contentView.findViewById(R.id.river_rate_spinner);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
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

        mRiverRateSpinner.setAdapter(new RiverRateAdatper());
        mRiverRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Preference.getInstance(mContext).setRiverRate(RIVER_RATE_ARRAY[position] * 1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        int index = 0;
        for(int i = 0; i < RIVER_RATE_ARRAY.length; i++){
            if(Preference.getInstance(mContext).getRiverRate() == RIVER_RATE_ARRAY[i] * 1000){
                index = i;
                break;
            }
        }
        mRiverRateSpinner.setSelection(index);
    }


    class RiverRateAdatper extends BaseAdapter{

        @Override
        public int getCount() {
            return RIVER_RATE_ARRAY.length;
        }

        @Override
        public Integer getItem(int position) {
            return RIVER_RATE_ARRAY[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(mContext);
            tv.setText(RIVER_RATE_ARRAY[position] + "分钟/次");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,100);
            tv.setLayoutParams(lp);
            tv.setGravity(Gravity.CENTER);
            //tv.setBackgroundColor(Color.parseColor("#aa99aa"));
            convertView = tv;
            return convertView;
        }
    }
}
