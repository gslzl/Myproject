package com.example.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private LocationClientOption option;
    private int isFirstLocate = 0;
    private BaiduMap baiduMap;
    private MapView mapView;
    private LatLng myPos;
    private BitmapDescriptor bitmapDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//        Intent intent = getIntent();
//        double longitude=intent.getDoubleExtra("longitude",0);
//        double latitude = intent.getDoubleExtra("latitude",0);
        isFirstLocate=0;

        mLocationClient = new LocationClient(this);
        option = new LocationClientOption();
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocationListener());

        mapView = findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();

        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_my_loc);


        baiduMap.setMyLocationEnabled(true);
        initLocationClientOption(option);
        mLocationClient.start();
//        setGoodsPos(latitude,longitude);
    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){//接收到位置
            if(location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType()== BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }
        }
    }



    //移动到我的位置，并让“我”显示在地图上
    private void navigateTo(BDLocation location){


        LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
        myPos = ll;//获取我的位置坐标

        if(isFirstLocate<3){
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.newLatLngZoom(ll,18f);
            baiduMap.animateMapStatus(update);
            isFirstLocate ++;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.accuracy(location.getRadius());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor);
        baiduMap.setMyLocationConfigeration(configuration);

    }

    public void setGoodsPos(double longitude, double latitude){

        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        BitmapDescriptor goodsLocMark = BitmapDescriptorFactory.fromResource(R.drawable.icon_goods_loc);

            OverlayOptions ele =  new MarkerOptions().position(new LatLng(latitude,longitude)).icon(goodsLocMark);
            options.add(ele);

        baiduMap.addOverlays(options);
    }




    private void initLocationClientOption(LocationClientOption option){
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setScanSpan(1001);
        option.setIsNeedAddress(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

    }
}
