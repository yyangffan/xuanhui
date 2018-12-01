package com.xuanhui.first.lock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.xuanhui.first.R;
import com.xuanhui.first.databinding.ActivityMapViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/11/29.
 */

public class MapViewLock extends BaseLock<ActivityMapViewBinding>{

    private AMap mMap;
    private  List<Marker> mMarkerLis;
    LatLngBounds.Builder boundsBuilder ;//存放所有点的经纬度

    public MapViewLock(Context context, ActivityMapViewBinding activityMapViewBinding) {
        super(context, activityMapViewBinding);
    }

    @Override
    public void init() {
        mMap = mBinding.mapview.getMap();
        mMarkerLis=new ArrayList<>();
        View v= LayoutInflater.from(mContext).inflate(R.layout.marker_layout,null);
        BitmapDescriptor bitmapDescriptor =BitmapDescriptorFactory.fromView(v);
        boundsBuilder = new LatLngBounds.Builder();
        for(int i=0;i<5;i++){
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(40.000207 + i, 116.795761 + i)).title("轩慧").icon(bitmapDescriptor));
            mMarkerLis.add(marker);
            boundsBuilder.include(marker.getPosition());//把所有点都include进去（LatLng类型）
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));//第二个参数为四周留空宽度
        mBinding.mapviewBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boundsBuilder = new LatLngBounds.Builder();
                for(Marker marker:mMarkerLis){
                    marker.setPosition(new LatLng(43.000207,marker.getPosition().longitude));
                    boundsBuilder.include(marker.getPosition());//把所有点都include进去（LatLng类型）
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));//第二个参数为四周留空宽度
            }
        });


    }
}
