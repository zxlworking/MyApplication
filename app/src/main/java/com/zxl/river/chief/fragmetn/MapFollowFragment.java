package com.zxl.river.chief.fragmetn;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

/**
 * Created by mac on 17-11-20.
 */

public class MapFollowFragment extends BaseFragment implements LocationSource,AMapLocationListener {

    private AMap aMap;
    private MapView mapView;
    //以前的定位点
    private LatLng oldLatLng;
    //是否是第一次定位
    private boolean isFirstLatLng;

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    @Override
    public int getContentView() {
        return R.layout.map_follow_fragment;
    }

    @Override
    public void initView(View contentView,Bundle savedInstanceState) {
        mapView = (MapView) contentView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        isFirstLatLng = true;
        init();
    }

    @Override
    public void initData() {

    }

    private  void init(){
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        //定位
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示


        /*
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位 LOCATION_TYPE_LOCATE、跟随 LOCATION_TYPE_MAP_FOLLOW 或地图根据面向方向旋转 LOCATION_TYPE_MAP_ROTATE
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
        */


        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);

        myLocationStyle.strokeColor(Color.parseColor("#000000ff"));//设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(Color.parseColor("#00aa99aa"));//设置定位蓝点精度圆圈的填充颜色的方法。

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        // 缩放级别（zoom）：地图缩放级别范围为【4-20级】，值越大地图越详细
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        /*
        //画线
        //使用 aMap.setMapTextZIndex(2) 可以将地图底图文字设置在添加的覆盖物之上
        aMap.setMapTextZIndex(2);
        //setUpMap(new LatLng(43.828, 87.621), new LatLng(43.800, 87.621));
        */

    }
    /**绘制两个坐标点之间的线段,从以前位置到现在位置*/
    private void setUpMap(LatLng oldData,LatLng newData ) {

        // 绘制一个大地曲线
        aMap.addPolyline((new PolylineOptions())
                .add(oldData, newData)
                .geodesic(true).color(Color.GREEN));

    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {

                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //定位成功
                //amapLocation.getLatitude();//获取经度
                //amapLocation.getLongitude();//获取纬度;

                LatLng newLatLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
//                Log.e("Amap", amapLocation.getLatitude() + "," + amapLocation.getLongitude());
//                Toast.makeText(this, amapLocation.getLatitude() + "," + amapLocation.getLongitude() , Toast.LENGTH_SHORT).show();

                float mZoom = aMap.getCameraPosition().zoom;
                //System.out.println("zxl--->zoom1--->"+mZoom+"--->"+isFirstLatLng);

                if(isFirstLatLng){
                    //记录第一次的定位信息
                    oldLatLng = newLatLng;
                    isFirstLatLng = false;


                    if(mZoom < 15){
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                    }
                }else{

                }
                //位置有变化
                if(oldLatLng != newLatLng){
                    Log.e("Amap", amapLocation.getLatitude() + "," + amapLocation.getLongitude());
                    setUpMap( oldLatLng , newLatLng );
                    oldLatLng = newLatLng;
                }

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
//                Toast.makeText(this, errText, Toast.LENGTH_SHORT).show();
                if(isFirstLatLng){
                    Toast.makeText(mContext, errText, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(false);
            /**
             * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
             * 注意：只有在高精度模式下的单次定位有效，其他方式无效
             */
            mLocationOption.setGpsFirst(true);
            // 设置发送定位请求的时间间隔,最小值为1000ms,1秒更新一次定位信息
            mLocationOption.setInterval(2000);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }
}
