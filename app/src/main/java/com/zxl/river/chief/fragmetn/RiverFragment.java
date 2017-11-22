package com.zxl.river.chief.fragmetn;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.zxl.river.chief.R;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

/**
 * Created by uidq0955 on 2017/11/22.
 */

public class RiverFragment extends BaseFragment {
    private static final String TAG = "RiverFragment";

    private static final float DEFAULT_ZOOM_VALUE = 15;
    private static final long DEFAULT_LOOP_UPLOAD_TIME = 2000;

    private MapView mMapView;
    private AMap mAMap;
    private MyLocationStyle mMyLocationStyle;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;

    private ImageView mStartPauseRiverImg;

    private boolean isStartRiver = false;
    private boolean isFirstLoaction = true;

    //上次的定位点
    private LatLng mLastLatLng;

    private Handler mHandler = new Handler(){};

    @Override
    public int getContentView() {
        return R.layout.river_fragment;
    }

    @Override
    public void initView(View contentView, Bundle savedInstanceState) {
        mMapView = (MapView) contentView.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);

        mStartPauseRiverImg = (ImageView) contentView.findViewById(R.id.start_pause_river_img);

        mStartPauseRiverImg.setOnClickListener(mOnClickListener);
    }

    @Override
    public void initData() {
        if(null == mAMap){
            mAMap = mMapView.getMap();
        }
        //显示默认定位按钮
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);
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
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_pause_river_img:
                    if(isStartRiver){
                        //停止
                        isStartRiver = false;
                        mStartPauseRiverImg.setImageResource(R.mipmap.ic_start_river);

                        mAMapLocationClientOption.setOnceLocation(true);

                        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);

                        mAMapLocationClient.startLocation();
                    }else{
                        //开始
                        isStartRiver = true;
                        mStartPauseRiverImg.setImageResource(R.mipmap.ic_pause_river);

                        mAMapLocationClientOption.setOnceLocation(false);
                        mAMapLocationClientOption.setInterval(DEFAULT_LOOP_UPLOAD_TIME);

                        mAMapLocationClient.setLocationOption(mAMapLocationClientOption);

                        mAMapLocationClient.startLocation();
                    }
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

                mLastLatLng = null;
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
                    if(null == mLastLatLng){
                        mLastLatLng = mCurrentLatLng;
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
            }
        }
    };

    /**
     * 绘制两个坐标点之间的线段,从以前位置到现在位置
     */
    private void setUpMap(LatLng oldData,LatLng newData ) {
        // 绘制一个大地曲线
        mAMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.GREEN));
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
    }
}
