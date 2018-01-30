package com.cn.wylee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

/**
 * Created by Administrator on 2018/1/30.
 */

public class MapActivity extends AppCompatActivity{
    private MapView mBaiduMap = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        mBaiduMap = findViewById(R.id.bmapView);
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient??
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        setMap();
    }

    private void setMap() {
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);// 设置地图的缩放比例
        mBaiduMap.getMap().setMapStatus(msu);
        location();
    }


    private void location() {
        // TODO Auto-generated method stub
        System.out.println("开始定位");
        // 设置定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//默认高精度，设置定位模式，高精度，低功耗，仅设??
        option.setCoorType("bd09ll");// 可??，默认gcj02，设置返回的定位结果坐标??
        int span = 1000;
        option.setScanSpan(span);// 可??，默??，即仅定位一次，设置发起定位请求的间隔需要大于等??000ms才是有效??
        option.setIsNeedAddress(true);// 可??，设置是否需要地????息，默认不需??
        option.setOpenGps(true);// 可??，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可??，默认false，设置是否当gps有效时按??S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);// 可??，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认????
        option.SetIgnoreCacheException(false);// 可??，默认false，设置是否收集CRASH信息，默认收??
        mLocationClient.setLocOption(option);
        // 打开定位图层
        mBaiduMap.getMap().setMyLocationEnabled(true);
        // 设置定位配置
        /**
         * BitmapDescriptor customMarker 用户自定义定位图?? boolean enableDirection
         * 是否允许显示方向信息 MyLocationConfiguration.LocationMode locationMode 定位图层显示方式
         */
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
        mBaiduMap.getMap().setMyLocationConfiguration(config);
    }
    private MyLocationData locaiontData;
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            locaiontData = new MyLocationData.Builder()
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
                try {
                    if (mBaiduMap != null) {
                        mBaiduMap.getMap().setMyLocationData(locaiontData);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mBaiduMap.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mLocationClient.start();
        mBaiduMap.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mBaiduMap.onPause();
    }

}
