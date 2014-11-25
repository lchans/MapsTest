package com.example.lavenderc.mapstest;
import com.google.maps.android.clustering;

/**
 * Created by lavenderc on 11/26/14.
 */
public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}