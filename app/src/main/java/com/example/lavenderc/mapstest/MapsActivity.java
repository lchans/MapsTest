package com.example.lavenderc.mapstest;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.GpsStatus;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.CircleOptions;

import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.UiSettings;

import java.util.ArrayList;
import java.util.List;



public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Address> addresses = null;
    private CharSequence text;
    private boolean isShapeSet = true;
    private Circle circle;
    private ArrayList<Circle> circles = new ArrayList<Circle>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                changeMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
       /*
            Set up initial stuff here. Used to be a marker.
        */
    }

    public void buttonAction (View view) {
       isShapeSet = !isShapeSet;
       for (Circle c: circles) {
            c.setVisible(isShapeSet);
       }
        Button myButton = (Button) findViewById(R.id.my_button);
        if (isShapeSet) {
            myButton.setText("Press to hide!");
        } else {
            myButton.setText("Press to show!");
        }

    }

    private void changeMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        GoogleMap.OnMapClickListener list = new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latLng) {
                if (isShapeSet) {
                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(latLng.latitude, latLng.longitude))
                            .radius(100000) // In meters
                            .strokeWidth(1)
                            .fillColor(Color.CYAN);
                    Circle circle = mMap.addCircle(circleOptions);
                    circles.add(circle);
                }
            }
        };
        mMap.setOnMapClickListener(list);

    }
}
