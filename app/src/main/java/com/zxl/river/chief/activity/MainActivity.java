package com.zxl.river.chief.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxl.river.chief.R;
import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.fragmetn.CountFragment;
import com.zxl.river.chief.fragmetn.EventFragment;
import com.zxl.river.chief.fragmetn.MeFragment;
import com.zxl.river.chief.fragmetn.NotificationFragment;
import com.zxl.river.chief.fragmetn.RecordFragment;
import com.zxl.river.chief.fragmetn.RiverFragment;
import com.zxl.river.chief.fragmetn.SettingsFragment;
import com.zxl.river.chief.http.HttpUtil;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.internal.util.ObserverSubscriber;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private static final int DEFAULT_INDEX = -1;
    private static final int RIVER_INDEX = 0;
    private static final int EVENT_INDEX = 1;
    private static final int RECORD_INDEX = 2;
    private static final int NOTIFICATION_INDEX = 3;
    private static final int SETTINGS_INDEX = 4;

    private static final String[] TITLE_ARRAY = new String[]{"巡河","事件","日志","通知","设置"};

    private ViewPager mViewPager;

    //private CustomAdapter mCustomAdapter;

    private List<Fragment> mFragments = new ArrayList<>();

    private LinearLayout mRiverLl;
    private LinearLayout mEventLl;
    private LinearLayout mRecordLl;
    private LinearLayout mNotificationLl;
    private LinearLayout mSettingsLl;

    private ImageView mRiverImg;
    private ImageView mEventImg;
    private ImageView mRecordImg;
    private ImageView mNotificationImg;
    private ImageView mMainSettingsImg;


    private TextView mRiverTv;
    private TextView mEventTv;
    private TextView mRecordTv;
    private TextView mNotificationTv;
    private TextView mSettingsTv;

    private int mCurrentIndex = DEFAULT_INDEX;

    private RiverFragment mRiverFragment;

    @Override
    public int getContentView() {
        return R.layout.main_activity;
    }

    @Override
    public void initView() {
        super.initView();

        mRiverFragment = new RiverFragment();
        mFragments.add(mRiverFragment);
        mFragments.add(new EventFragment());
        mFragments.add(new RecordFragment());
        mFragments.add(new NotificationFragment());
        mFragments.add(new SettingsFragment());


        mRiverLl = (LinearLayout) findViewById(R.id.river_ll);
        mEventLl = (LinearLayout) findViewById(R.id.event_ll);
        mRecordLl = (LinearLayout) findViewById(R.id.record_ll);
        mNotificationLl = (LinearLayout) findViewById(R.id.notification_ll);
        mSettingsLl = (LinearLayout) findViewById(R.id.settings_ll);

        mRiverImg = (ImageView) findViewById(R.id.river_img);
        mEventImg = (ImageView) findViewById(R.id.event_img);
        mRecordImg = (ImageView) findViewById(R.id.record_img);
        mNotificationImg = (ImageView) findViewById(R.id.notification_img);
        mMainSettingsImg = (ImageView) findViewById(R.id.main_settings_img);


        mRiverTv = (TextView) findViewById(R.id.river_tv);
        mEventTv = (TextView) findViewById(R.id.event_tv);
        mRecordTv = (TextView) findViewById(R.id.record_tv);
        mNotificationTv = (TextView) findViewById(R.id.notification_tv);
        mSettingsTv = (TextView) findViewById(R.id.settings_tv);


        /*
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCustomAdapter = new CustomAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCustomAdapter);

        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        */

        setLeftImgVisibility(View.GONE);
        setRightImgVisibility(View.GONE);

        setTab(RIVER_INDEX);

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.river_ll:
                        setTab(RIVER_INDEX);
                        break;
                    case R.id.event_ll:
                        setTab(EVENT_INDEX);
                        break;
                    case R.id.record_ll:
                        setTab(RECORD_INDEX);
                        break;
                    case R.id.notification_ll:
                        setTab(NOTIFICATION_INDEX);
                        break;
                    case R.id.settings_ll:
                        setTab(SETTINGS_INDEX);
                        break;
                    case R.id.title_bar_right_img:
                        if(mCurrentIndex == EVENT_INDEX){
                            Intent mUploadEventIntent = new Intent(mContext,UploadEventActivity.class);
                            mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mUploadEventIntent);
                        }
                        if(mCurrentIndex == RECORD_INDEX){
                            Intent mUploadEventIntent = new Intent(mContext,CountActivity.class);
                            mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mUploadEventIntent);
                        }
                        break;
                }
            }
        };

        mRiverLl.setOnClickListener(mOnClickListener);
        mEventLl.setOnClickListener(mOnClickListener);
        mRecordLl.setOnClickListener(mOnClickListener);
        mNotificationLl.setOnClickListener(mOnClickListener);
        mSettingsLl.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {
        super.initData();
        /*HandlerThread mHandlerThread = new HandlerThread("test");
        mHandlerThread.start();
        Subscription test = HttpUtil.test("value1", "value2", new Handler(), new Subscriber<ResponseBody>() {
            @Override
            public void onStart() {
                super.onStart();
                DebugUtils.d(TAG,"HttpUtil.test::onStart--->"+Thread.currentThread());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    DebugUtils.d(TAG,"HttpUtil.test::onNext--->"+Thread.currentThread()+"--->"+responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                DebugUtils.d(TAG,"HttpUtil.test::onError--->"+Thread.currentThread()+"--->"+e);
            }

            @Override
            public void onCompleted() {
                DebugUtils.d(TAG,"HttpUtil.test::onCompleted--->"+Thread.currentThread());
            }
        });*/
    }


    private void setTab(int index){

        DebugUtils.d(TAG,"setTab::index = " + index);
        DebugUtils.d(TAG,"setTab::mCurrentIndex = " + mCurrentIndex);
        if(mCurrentIndex == index){
            return;
        }
        mCurrentIndex = index;

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

        if(mCurrentIndex == RECORD_INDEX){
            setRightImgRes(R.mipmap.ic_count_selected);
        }else if(mCurrentIndex == EVENT_INDEX){
            setRightImgRes(R.mipmap.ic_upload_event_info);
        }else{
            setRightImgVisibility(View.GONE);
        }

        mRiverImg.setImageResource(index != RIVER_INDEX ? R.mipmap.ic_river_normal : R.mipmap.ic_river_selected);
        mRiverTv.setTextColor(index != RIVER_INDEX ? Color.parseColor("#909090") : Color.parseColor("#199bff"));
        mEventImg.setImageResource(index != EVENT_INDEX ? R.mipmap.ic_event_normal : R.mipmap.ic_event_selected);
        mEventTv.setTextColor(index != EVENT_INDEX ? Color.parseColor("#909090") : Color.parseColor("#199bff"));
        mRecordImg.setImageResource(index != RECORD_INDEX ? R.mipmap.ic_event_normal : R.mipmap.ic_event_selected);
        mRecordTv.setTextColor(index != RECORD_INDEX ? Color.parseColor("#909090") : Color.parseColor("#199bff"));
        mNotificationImg.setImageResource(index != NOTIFICATION_INDEX ? R.mipmap.ic_notification_normal : R.mipmap.ic_notification_selected);
        mNotificationTv.setTextColor(index != NOTIFICATION_INDEX ? Color.parseColor("#909090") : Color.parseColor("#199bff"));
        mMainSettingsImg.setImageResource(index != SETTINGS_INDEX ? R.mipmap.ic_settings_normal : R.mipmap.ic_settings_selected);
        mSettingsTv.setTextColor(index != SETTINGS_INDEX ? Color.parseColor("#909090") : Color.parseColor("#199bff"));


        setTitle(TITLE_ARRAY[index]);

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mRiverFragment.onKeyDown(keyCode,event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
