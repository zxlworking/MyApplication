package com.zxl.river.chief.fragmetn;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.zxl.river.chief.R;
import com.zxl.river.chief.activity.UploadEventActivity;
import com.zxl.river.chief.activity.UploadRiverActivity;
import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by uidq0955 on 2017/11/22.
 */

public class RiverFragment extends BaseFragment {
    private static final String TAG = "RiverFragment";

    private static final float DEFAULT_ZOOM_VALUE = 15;
    private static final long DEFAULT_LOOP_UPLOAD_TIME = 2000;
    private static final long DEFAULT_COUNT_RIVER_TIME_DELAY = 1000;

    private static final int MSG_START_COUNT_RIVER_TIME = 1;

    private MapView mMapView;
    private AMap mAMap;
    private MyLocationStyle mMyLocationStyle;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;

    private FrameLayout mStartPauseRiverFl;
    private FrameLayout mUploadRiverInfoFl;
    private FrameLayout mUploadEventInfoFl;

    private ImageView mStartPauseRiverImg;
    private TextView mStartRiverTv;

    private TextView mRiverTimeTv;
    private TextView mRiverDistanceTv;
    private TextView mRiverStartTimeTv;
    private TextView mRiverEndTimeTv;

    private boolean isStartRiver = false;
    private boolean isFirstLoaction = true;

    private FrameLayout mStartRiverContentFl;
    private LinearLayout mEndRiverContentLl;
    private LinearLayout mEndRiverDateContentLl;
    private LinearLayout mRiverInfoContentLl;

    //上次的定位点
    private LatLng mLastLatLng;

    private DecimalFormat mRiverCountFormat = new DecimalFormat("00");
    private DecimalFormat mRiverDistanceFormat = new DecimalFormat("#0.00");
    private SimpleDateFormat mRiverTimeFormat = new SimpleDateFormat("HH:mm");

    private long mRiverCountTime = 0;
    private float mRiverDistance = 0;
    private long mRiverStartTime = 0;
    private long mRiverEndTime = 0;

    private RequestLocationPermissionReceiver mRequestLocationPermissionReceiver;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_START_COUNT_RIVER_TIME:
                    mRiverCountTime++;

                    long mHour = mRiverCountTime / 3600;
                    long mMinute = (mRiverCountTime % 3600) / 60;
                    long mSecond = (mRiverCountTime % 3600) % 60;

                    //mRiverTimeTv.setText(String.format("%02d", mHour)+":"+String.format("%02d", mMinute)+":"+String.format("%02d", mSecond));
                    mRiverTimeTv.setText(mRiverCountFormat.format(mHour)+":"+mRiverCountFormat.format(mMinute)+":"+mRiverCountFormat.format(mSecond));

                    this.sendEmptyMessageDelayed(MSG_START_COUNT_RIVER_TIME,DEFAULT_COUNT_RIVER_TIME_DELAY);
                    break;
            }
        }
    };

    @Override
    public int getContentView() {
        return R.layout.river_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        mMapView = (MapView) contentView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mStartPauseRiverFl = (FrameLayout) contentView.findViewById(R.id.start_pause_river_fl);
        mUploadRiverInfoFl = (FrameLayout) contentView.findViewById(R.id.upload_river_info_fl);
        mUploadEventInfoFl = (FrameLayout) contentView.findViewById(R.id.upload_event_info_fl);

        mStartPauseRiverImg = (ImageView) contentView.findViewById(R.id.start_pause_river_img);
        mStartRiverTv = (TextView) contentView.findViewById(R.id.start_river_tv);

        mRiverTimeTv = (TextView) contentView.findViewById(R.id.river_time_tv);
        mRiverDistanceTv = (TextView) contentView.findViewById(R.id.river_distance_tv);
        mRiverStartTimeTv = (TextView) contentView.findViewById(R.id.river_start_time_tv);
        mRiverEndTimeTv = (TextView) contentView.findViewById(R.id.river_end_time_tv);

        mStartRiverContentFl = (FrameLayout) contentView.findViewById(R.id.start_river_content_fl);
        mEndRiverContentLl = (LinearLayout) contentView.findViewById(R.id.end_river_content_ll);
        mEndRiverDateContentLl = (LinearLayout) contentView.findViewById(R.id.end_river_date_content_ll);
        mRiverInfoContentLl = (LinearLayout) contentView.findViewById(R.id.rivier_info_content_ll);

        mStartRiverTv.setOnClickListener(mOnClickListener);
        mStartPauseRiverFl.setOnClickListener(mOnClickListener);
        mUploadRiverInfoFl.setOnClickListener(mOnClickListener);
        mUploadEventInfoFl.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {

        mRequestLocationPermissionReceiver = new RequestLocationPermissionReceiver();
        IntentFilter mIntentFilter = new IntentFilter(Constants.ACTION_GET_LOCATION_PERMISSION);
        mContext.registerReceiver(mRequestLocationPermissionReceiver,mIntentFilter);

        if(null == mAMap){
            mAMap = mMapView.getMap();
        }
        //显示默认定位按钮
        //mAMap.getUiSettings().setMyLocationButtonEnabled(true);
        //设置定位监听
        mAMap.setLocationSource(mLocationSource);

        mMyLocationStyle = new MyLocationStyle();

        /*
        mMyLocationStyle.interval(2000);
        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        */

        //设置定位蓝点精度圆圈的边框颜色的方法。
        mMyLocationStyle.strokeColor(Color.parseColor("#000000ff"));
        //设置定位蓝点精度圆圈的填充颜色的方法。
        mMyLocationStyle.radiusFillColor(Color.parseColor("#00aa99aa"));
        //设置定位蓝点的Style
        mAMap.setMyLocationStyle(mMyLocationStyle);

        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setMyLocationEnabled(true);

        // 缩放级别（zoom）：地图缩放级别范围为【4-20级】，值越大地图越详细
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_VALUE));

        for(int i = 0; i < 7; i++){
            Calendar mCalendar = Calendar.getInstance();
            mCalendar.add(Calendar.DAY_OF_MONTH,-3 + i);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("zxl--->date--->"+sf.format(mCalendar.getTime()));


            View mDateItemView = LayoutInflater.from(mContext).inflate(R.layout.end_river_date_item_view,null);
            TextView mEndRiverDateDayTv = (TextView) mDateItemView.findViewById(R.id.end_river_date_day_tv);
            TextView mEndRiverDateWeekTv = (TextView) mDateItemView.findViewById(R.id.end_river_date_week_tv);

            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            mEndRiverDateDayTv.setText(""+day);
            int week = mCalendar.get(Calendar.DAY_OF_WEEK);
            if(i == 7/2){
                mEndRiverDateWeekTv.setText("今天");
            }else{
                String weekStr = "";
                switch (week){
                    case 1:
                        weekStr = "周一";
                        break;
                    case 2:
                        weekStr = "周二";
                        break;
                    case 3:
                        weekStr = "周三";
                        break;
                    case 4:
                        weekStr = "周四";
                        break;
                    case 5:
                        weekStr = "周五";
                        break;
                    case 6:
                        weekStr = "周六";
                        break;
                    case 7:
                        weekStr = "周天";
                        break;

                }
                mEndRiverDateWeekTv.setText(weekStr);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            mDateItemView.setLayoutParams(lp);
            mEndRiverDateContentLl.addView(mDateItemView);
        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_river_tv:
                case R.id.start_pause_river_fl:
                    DebugUtils.d(TAG,"onClick::start_pause_river_fl::isStartRiver = " + isStartRiver);
                    if(isStartRiver){
                        //停止
                        isStartRiver = false;
                        mStartPauseRiverImg.setImageResource(R.mipmap.ic_start_river);
                        addMark(R.mipmap.ic_map_stop,mLastLatLng);

                        mAMapLocationClientOption.setOnceLocation(true);
                        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
                        mAMapLocationClient.stopLocation();

                        mHandler.removeMessages(MSG_START_COUNT_RIVER_TIME);

                        mRiverEndTime = new Date().getTime();
                        mRiverEndTimeTv.setText("结束："+mRiverTimeFormat.format(new Date(mRiverEndTime)));

                        mEndRiverContentLl.setVisibility(View.VISIBLE);
                        mStartRiverContentFl.setVisibility(View.GONE);

                    }else{
                        //开始
                        isStartRiver = true;
                        mLastLatLng = null;
                        mStartPauseRiverImg.setImageResource(R.mipmap.ic_pause_river);


                        mRiverCountTime = 0;
                        mHandler.removeMessages(MSG_START_COUNT_RIVER_TIME);
                        mHandler.sendEmptyMessage(MSG_START_COUNT_RIVER_TIME);
                        mRiverDistance = 0;

                        mRiverStartTime = new Date().getTime();
                        mRiverEndTime = 0;

                        mRiverDistanceTv.setText("0.00");
                        mRiverStartTimeTv.setText("开始："+mRiverTimeFormat.format(new Date(mRiverStartTime)));
                        mRiverEndTimeTv.setText("结束：00:00");

                        mAMapLocationClientOption.setOnceLocation(false);
                        mAMapLocationClientOption.setInterval(DEFAULT_LOOP_UPLOAD_TIME);
                        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
                        mAMapLocationClient.startLocation();

                        mRiverInfoContentLl.setVisibility(View.VISIBLE);

                        mEndRiverContentLl.setVisibility(View.GONE);
                        mStartRiverContentFl.setVisibility(View.VISIBLE);

                    }
                    break;
                case R.id.upload_river_info_fl:
                    DebugUtils.d(TAG,"onClick::upload_river_info_fl");
                    DebugUtils.d(TAG,"onClick::upload_event_info_fl");
                    Intent mUploadRiverIntent = new Intent(mContext,UploadRiverActivity.class);
                    mUploadRiverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mUploadRiverIntent.putExtra(UploadRiverActivity.EXTRA_RIVER_START_TIME,mRiverStartTime);
                    mUploadRiverIntent.putExtra(UploadRiverActivity.EXTRA_RIVER_END_TIME,mRiverEndTime);
                    startActivity(mUploadRiverIntent);
                    break;
                case R.id.upload_event_info_fl:
                    DebugUtils.d(TAG,"onClick::upload_event_info_fl");
                    Intent mUploadEventIntent = new Intent(mContext,UploadEventActivity.class);
                    mUploadEventIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mUploadEventIntent);
                    break;
            }
        }
    };

    private  LocationSource mLocationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            DebugUtils.d(TAG,"LocationSource::activate");
            DebugUtils.d(TAG,"LocationSource::activate::mContext = " + mContext);
            mOnLocationChangedListener = onLocationChangedListener;
            if(null == mAMapLocationClient){
                mAMapLocationClient = new AMapLocationClient(mContext);
                mAMapLocationClientOption = new AMapLocationClientOption();

                //设置定位监听
                mAMapLocationClient.setLocationListener(mLocationListener);
                //设置为高精度定位模式
                mAMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mAMapLocationClientOption.setOnceLocation(true);
                /**
                 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
                 * 注意：只有在高精度模式下的单次定位有效，其他方式无效
                 */
                //mAMapLocationClientOption.setGpsFirst(true);

                //设置定位参数
                mAMapLocationClient.setLocationOption(mAMapLocationClientOption);

                mAMapLocationClient.startLocation();
            }
        }

        @Override
        public void deactivate() {
            DebugUtils.d(TAG,"LocationSource::deactivate");
            mOnLocationChangedListener = null;
            if (mAMapLocationClient != null) {
                mAMapLocationClient.stopLocation();
                mAMapLocationClient.onDestroy();
                mAMapLocationClient = null;

            }
        }
    };

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation aMapLocation) {
            DebugUtils.d(TAG,"AMapLocationListener::onLocationChanged");

            if (mOnLocationChangedListener != null && aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                if(isFirstLoaction){
                    isFirstLoaction = false;

                    final float mZoom = mAMap.getCameraPosition().zoom;
                    DebugUtils.d(TAG,"AMapLocationListener::onLocationChanged::mZoom = " + mZoom);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mOnLocationChangedListener.onLocationChanged(aMapLocation);
                            //将地图移动到定位点
                            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                            if(mZoom < DEFAULT_ZOOM_VALUE){
                                mAMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_VALUE));
                            }
                        }
                    },500);

                }else{
                    mOnLocationChangedListener.onLocationChanged(aMapLocation);
                    //将地图移动到定位点
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                }

                LatLng mCurrentLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                if(isStartRiver){
                    DebugUtils.d(TAG,"AMapLocationListener::onLocationChanged::mLastLatLng = " + mLastLatLng);
                    if(null == mLastLatLng){
                        mLastLatLng = mCurrentLatLng;
                        mAMap.clear();
                        addMark(R.mipmap.ic_map_start,mLastLatLng);
                    }else if(mLastLatLng != null && mLastLatLng != mCurrentLatLng){
                        setUpMap( mLastLatLng , mCurrentLatLng );
                        mLastLatLng = mCurrentLatLng;
                    }
                }
            }else{
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                DebugUtils.d(TAG,"AMapLocationListener::onLocationChanged::errText = " + errText);
                if(isFirstLoaction){
                    CommonUtils.showMessage(mContext, errText, Toast.LENGTH_SHORT);
                }
                if(aMapLocation.getErrorCode() == AMapLocation.ERROR_CODE_FAILURE_LOCATION_PERMISSION){
                    boolean mPermissionResult = CommonUtils.selfPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, mContext.getApplicationContext());
                    DebugUtils.d(TAG,"AMapLocationListener::onLocationChanged::mPermissionResult = " + mPermissionResult);
                    if(!mPermissionResult){
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},Constants.LOCATION_STATE);
                    }
                }
            }
        }
    };

    /**
     * 绘制两个坐标点之间的线段,从以前位置到现在位置
     */
    private void setUpMap(LatLng oldData,LatLng newData ) {
        if(null == oldData || !isStartRiver){
            mLastLatLng = null;
            return;
        }
        // 绘制一个大地曲线
        mAMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.GREEN));

        mRiverDistance = mRiverDistance + Math.abs(AMapUtils.calculateLineDistance(oldData,newData));
        mRiverDistanceTv.setText(mRiverDistanceFormat.format(mRiverDistance/1000));
    }

    private void addMark(int resId,LatLng latLng){
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        //markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");

        markerOption.draggable(true);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),resId)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        mAMap.addMarker(markerOption);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if(null != mAMapLocationClient){
            mAMapLocationClient.onDestroy();
        }

        mHandler.removeMessages(MSG_START_COUNT_RIVER_TIME);

        if(mRequestLocationPermissionReceiver != null){
            mContext.unregisterReceiver(mRequestLocationPermissionReceiver);
        }
    }

    class RequestLocationPermissionReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            DebugUtils.d(TAG,"RequestLocationPermissionReceiver::onReceive::mAction = " + mAction);
            if(TextUtils.equals(mAction,Constants.ACTION_GET_LOCATION_PERMISSION)){
                mAMapLocationClientOption.setOnceLocation(true);
                mAMapLocationClient.setLocationOption(mAMapLocationClientOption);
                mAMapLocationClient.startLocation();
            }
        }
    }
}
