package com.zxl.river.chief.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
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
import com.zxl.river.chief.common.Constants;
import com.zxl.river.chief.utils.CommonUtils;
import com.zxl.river.chief.utils.DebugUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 17-12-3.
 */

public class TrajectoryPlaybackActivity extends BaseActivity {

    private static final String TAG = "TrajectoryPlaybackActiv";

    private static final float DEFAULT_ZOOM_VALUE = 15;
    private static final long DEFAULT_LOOP_UPLOAD_TIME = 2000;

    private MapView mTrajectoryPlaybackMapView;
    private AMap mAMap;
    private MyLocationStyle mMyLocationStyle;
    private LocationSource.OnLocationChangedListener mOnLocationChangedListener;
    private AMapLocationClient mAMapLocationClient;
    private AMapLocationClientOption mAMapLocationClientOption;

    private boolean isFirstLoaction = true;

    private Handler mHandler = new Handler();

    @Override
    public int getContentView() {
        return R.layout.trajectory_playback_activity;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView();
        setRightImgVisibility(View.GONE);
        setTitle("轨迹回放");

        mTrajectoryPlaybackMapView = (MapView) findViewById(R.id.trajectory_playback_map_view);
        mTrajectoryPlaybackMapView.onCreate(savedInstanceState);

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


        if(null == mAMap){
            mAMap = mTrajectoryPlaybackMapView.getMap();
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
    }


    private LocationSource mLocationSource = new LocationSource() {
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
                    DebugUtils.d(TAG,"AMapLocationListener::onLocationChanged::aMapLocation = " + aMapLocation);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mOnLocationChangedListener.onLocationChanged(aMapLocation);
                            //将地图移动到定位点
                            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                            if(mZoom < DEFAULT_ZOOM_VALUE){
                                mAMap.moveCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM_VALUE));
                            }
                            drawTrajectoryLine();
                        }
                    },500);

                }else{
                    mOnLocationChangedListener.onLocationChanged(aMapLocation);
                    //将地图移动到定位点
                    mAMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    drawTrajectoryLine();
                }

                LatLng mCurrentLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

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
                        ActivityCompat.requestPermissions(TrajectoryPlaybackActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.LOCATION_STATE);
                    }
                }
            }
        }
    };

    private void drawTrajectoryLine(){
        //latitude=31.963616#longitude=118.821748

        List<LatLng> mLatLngs = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            LatLng mLatLng = new LatLng(31.963616 - 0.0001 * i, 118.821748);
            if(i == 0){
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatLng));
            }
            mLatLngs.add(mLatLng);
        }

        for(int i = 0; i < 100; i++){
            LatLng mLatLng = new LatLng(31.963616, 118.821748 - 0.0001 * i);
            mLatLngs.add(mLatLng);
        }
        for(int i = 0; i < 100; i++){
            LatLng mLatLng = new LatLng(31.963616 - 0.0001 * i, 118.821748 - 0.0001 * 100 + 0.0001 * i);
            mLatLngs.add(mLatLng);
        }

        PolylineOptions mPolylineOptions = new PolylineOptions();
        for(int i = 0; i < mLatLngs.size() - 1; i++){
            mPolylineOptions.add(mLatLngs.get(i));
        }
        mPolylineOptions.geodesic(true).color(Color.GREEN);
        mAMap.addPolyline(mPolylineOptions);

//        mAMap.addPolyline((new PolylineOptions())
//                .add(oldData, newData)
//                .geodesic(true).color(Color.GREEN));
    }

    @Override
    public void onResume() {
        super.onResume();
        mTrajectoryPlaybackMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTrajectoryPlaybackMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mTrajectoryPlaybackMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTrajectoryPlaybackMapView.onDestroy();

        if(null != mAMapLocationClient){
            mAMapLocationClient.onDestroy();
        }
    }
}
