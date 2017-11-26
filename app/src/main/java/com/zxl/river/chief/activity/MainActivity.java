package com.zxl.river.chief.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxl.river.chief.R;
import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.fragmetn.CountFragment;
import com.zxl.river.chief.fragmetn.MeFragment;
import com.zxl.river.chief.fragmetn.RiverFragment;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private static final int RIVER_INDEX = 0;
    private static final int COUNT_INDEX = 1;
    private static final int ME_INDEX = 2;

    private static final String[] TITLE_ARRAY = new String[]{"巡河","统计","我的"};

    private ViewPager mViewPager;

    //private CustomAdapter mCustomAdapter;

    private List<Fragment> mFragments = new ArrayList<>();

    private LinearLayout mRiverLl;
    private LinearLayout mCountLl;
    private LinearLayout mMeLl;

    private ImageView mRiverImg;
    private ImageView mCountImg;
    private ImageView mMeImg;
    private ImageView mBackImg;
    private ImageView mSettingsImg;

    private TextView mRiverTv;
    private TextView mCountTv;
    private TextView mMeTv;
    private TextView mTitleTv;

    private int mCurrentIndex = RIVER_INDEX;

    @Override
    public int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    public void initView() {
        mFragments.add(new RiverFragment());
        mFragments.add(new CountFragment());
        mFragments.add(new MeFragment());


        mRiverLl = (LinearLayout) findViewById(R.id.river_ll);
        mCountLl = (LinearLayout) findViewById(R.id.count_ll);
        mMeLl = (LinearLayout) findViewById(R.id.me_ll);

        mRiverImg = (ImageView) findViewById(R.id.river_img);
        mCountImg = (ImageView) findViewById(R.id.count_img);
        mMeImg = (ImageView) findViewById(R.id.me_img);
        mBackImg = (ImageView) findViewById(R.id.back_img);
        mSettingsImg = (ImageView) findViewById(R.id.settings_img);

        mRiverTv = (TextView) findViewById(R.id.river_tv);
        mCountTv = (TextView) findViewById(R.id.count_tv);
        mMeTv = (TextView) findViewById(R.id.me_tv);
        mTitleTv = (TextView) findViewById(R.id.title_tv);

        mRiverLl.setOnClickListener(mOnClickListener);
        mCountLl.setOnClickListener(mOnClickListener);
        mMeLl.setOnClickListener(mOnClickListener);
        mSettingsImg.setOnClickListener(mOnClickListener);

        /*
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCustomAdapter = new CustomAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCustomAdapter);

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        */

        mBackImg.setVisibility(View.GONE);

        setTab(RIVER_INDEX);
    }

    @Override
    public void initData() {

    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.river_ll:
                    //mViewPager.setCurrentItem(RIVER_INDEX);
                    setTab(RIVER_INDEX);
                    break;
                case R.id.count_ll:
                    //mViewPager.setCurrentItem(COUNT_INDEX);
                    setTab(COUNT_INDEX);
                    break;
                case R.id.me_ll:
                    //mViewPager.setCurrentItem(DEAL_INDEX);
                    setTab(ME_INDEX);
                    break;
                case R.id.settings_img:
                    Intent mSettingsIntent = new Intent(mContext,SettingsActivity.class);
                    mSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mSettingsIntent);
                    break;
            }
        }
    };

    private void setTab(int index){

        DebugUtils.d(TAG,"setTab::index = " + index);

        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        for(int i = 0; i < mFragments.size(); i++){
            Fragment mFragment = mFragments.get(i);
            if(i != index){
                if (mFragment.isAdded()){
                    mFragmentTransaction.hide(mFragment);
                }
            }else{
                if(mFragment.isAdded()){
                    mFragmentTransaction.show(mFragment);
                }else{
                    mFragmentTransaction.add(R.id.content_fl,mFragment);
                }
            }

        }
        mFragmentTransaction.commit();


        mTitleTv.setText(TITLE_ARRAY[index]);
        switch (index){
            case RIVER_INDEX:
                mRiverImg.setImageResource(R.mipmap.ic_river_selected);
                mRiverTv.setTextColor(Color.parseColor("#199bff"));
                mCountImg.setImageResource(R.mipmap.ic_count_normal);
                mCountTv.setTextColor(Color.parseColor("#909090"));
                mMeImg.setImageResource(R.mipmap.ic_me_normal);
                mMeTv.setTextColor(Color.parseColor("#909090"));
                break;
            case COUNT_INDEX:
                mRiverImg.setImageResource(R.mipmap.ic_river_normal);
                mRiverTv.setTextColor(Color.parseColor("#909090"));
                mCountImg.setImageResource(R.mipmap.ic_count_selected);
                mCountTv.setTextColor(Color.parseColor("#199bff"));
                mMeImg.setImageResource(R.mipmap.ic_me_normal);
                mMeTv.setTextColor(Color.parseColor("#909090"));
                break;
            case ME_INDEX:
                mRiverImg.setImageResource(R.mipmap.ic_river_normal);
                mRiverTv.setTextColor(Color.parseColor("#909090"));
                mCountImg.setImageResource(R.mipmap.ic_count_normal);
                mCountTv.setTextColor(Color.parseColor("#909090"));
                mMeImg.setImageResource(R.mipmap.ic_me_selected);
                mMeTv.setTextColor(Color.parseColor("#199bff"));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DebugUtils.d(TAG,"onRequestPermissionsResult::grantResults = " + grantResults);
        DebugUtils.d(TAG,"onRequestPermissionsResult::requestCode = " + requestCode);
        if(grantResults.length<=0){
            return;
        }
        DebugUtils.d(TAG,"onRequestPermissionsResult::grantResults[0] = " + grantResults[0]);
        if (requestCode == Constants.LOCATION_STATE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent mGetLocationPermissionIntent = new Intent(Constants.ACTION_GET_LOCATION_PERMISSION);
                sendBroadcast(mGetLocationPermissionIntent);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                CommonUtils.showMessage(mContext, "获取位置权限被禁用");
            }
        }

    }


    /*
    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };




    class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

    }
    */

}
