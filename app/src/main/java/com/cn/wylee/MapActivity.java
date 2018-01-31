package com.cn.wylee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.cn.wylee.bean.PioBean;
import com.cn.wylee.view.ShapeLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */

public class MapActivity extends AppCompatActivity{
    private MapView mBaiduMap = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = null;
    private  PoiSearch mPoiSearch;
    String TAG="lee";
    private LatLng latLng ;
    private List<PioBean> pioBeanList;
    private String PIO_KEY="PIO";
    private  ShapeLoadingDialog shapeLoadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                .loadText("加载地图中...")
                .build();
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
        shapeLoadingDialog.show();
       // shapeLoadingDialog.setTitle("加载地图中..");
        mBaiduMap = findViewById(R.id.bmapView);
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient??
        myListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myListener); // 注册监听函数
        setMap();
      //  serchPoi();


    }

    private void serchPoi() {
        pioBeanList=new ArrayList<>();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        /*mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("北京")
                .keyword("彩票")
                .pageNum(10));*/
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword("餐厅")
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(latLng)
                .radius(10000)
                .pageNum(10));
    }

    private void setMap() {
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);// 设置地图的缩放比例
        mBaiduMap.getMap().setMapStatus(msu);
        setMapListener();
        location();
    }


    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            PioBean bean;
                //获取POI检索结果
            List<PoiInfo> allPoi = poiResult.getAllPoi();

            if(null==allPoi||allPoi.size()==0){
                Toast.makeText(MapActivity.this,"周边搜索失败",Toast.LENGTH_SHORT).show();
                shapeLoadingDialog.dismiss();
                return;
            }
            for (int k=0;k<allPoi.size();k++){
                Log.d(TAG,allPoi.get(k).name);
                bean=new PioBean(allPoi.get(k).location.latitude,
                        allPoi.get(k).location.longitude,allPoi.get(k).address,allPoi.get(k).name);
                pioBeanList.add(bean);
            }
            addOverLay();
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            //获取Place详情页检索结果
            Log.d(TAG,poiDetailResult.getLocation().latitude+"");
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    };
    //添加标记点
    private void addOverLay() {
        new Thread() {
            public void run() {
                try {
                    LatLng latLng;
                    for (int i = 0; i < pioBeanList.size(); i++) {
                        PioBean pioBean = pioBeanList.get(i);
                        latLng=new LatLng(pioBean.getLatitude(),pioBean.getLongtitude());
                        // 构建Marker图标
                        BitmapDescriptor descriptor = BitmapDescriptorFactory
                                .fromResource(R.drawable.pio);
                        //marker选项
                        OverlayOptions option = new MarkerOptions()
                                .position(latLng).icon(descriptor).zIndex(i);
                        Marker marker = (Marker) mBaiduMap.getMap().addOverlay(option);
                        //将pio点和一个marker绑定
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(PIO_KEY, pioBean);
                        marker.setExtraInfo(bundle);
                    }
                    shapeLoadingDialog.dismiss();
                } catch (Exception e) {
                    Log.d(TAG,"添加pio点"+e.toString());
                    shapeLoadingDialog.dismiss();
                }
            }
        }.start();

    }
    /**
     * 设置地图点击监听
     */

    private void setMapListener() {
        mBaiduMap.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                mBaiduMap.getMap().hideInfoWindow();

            }
        });
        // 点击地图覆盖物时触发
        mBaiduMap.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // 从marker中取出对象
                PioBean pioBean = (PioBean) marker
                        .getExtraInfo().getParcelable(PIO_KEY);
                // 将报告摘要传递给弹出的popWindow
                showDetailPW(pioBean);
                return false;
            }
        });
    }

    private void showDetailPW(PioBean pioBean) {
        // 地球标准坐标
        LatLng latLng = new LatLng(pioBean.getLatitude(),
                pioBean.getLongtitude());
        View popWindow =LayoutInflater.from(this).inflate(R.layout.pio_popwindow,
                null);
        TextView tv_name=popWindow.findViewById(R.id.name);
        TextView tv_adress=popWindow.findViewById(R.id.adress);
        tv_name.setText(pioBean.getName());
        tv_adress.setText(pioBean.getAdress());
        // 创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(popWindow, latLng, -100);
        // 显示InfoWindow
        mBaiduMap.getMap().showInfoWindow(mInfoWindow);
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
            if(location.getLatitude()>3&&location.getLatitude()<54){
                latLng=  new LatLng(location.getLatitude(),location.getLongitude());
                locaiontData = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                try {
                    if (mBaiduMap != null) {
                        mBaiduMap.getMap().setMyLocationData(locaiontData);
                    }
                    serchPoi();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.d(TAG,e.toString());
                    shapeLoadingDialog.dismiss();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mBaiduMap.onDestroy();
        mPoiSearch.destroy();
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
