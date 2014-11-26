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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;

import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.UiSettings;
import android.location.LocationProvider;
import android.widget.Toast;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Address> addresses = null;
    private CharSequence text;
    private boolean isShapeSet;
    private Circle circle;

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
       if (isShapeSet) {
           CircleOptions circleOptions = new CircleOptions()
                   .center(new LatLng(37.4, -122.1))
                   .radius(100000) // In meters
                   .strokeWidth(1)
                   .fillColor(Color.CYAN);
           circle = mMap.addCircle(circleOptions);
       } else {
            circle.remove();
       }
       System.out.println(isShapeSet);
    }

    private void changeMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        mMap.setMyLocationEnabled(true);
        UiSettings settings = mMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        LocationProvider provider;
        GoogleMap.OnMapClickListener list = new GoogleMap.OnMapClickListener() {
            public void onMapClick(LatLng latLng) {
                Context context = getApplicationContext();
                Geocoder loc = new Geocoder(context, Locale.getDefault());
                if (loc.isPresent()) {
                    try {
                        addresses = loc.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (addresses.size() > 0) {
                            text = "Postal code: " + addresses.get(0);
                        } else {
                            text = "You're somewhere in the ocean";
                        }
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();
                    } catch  (IllegalArgumentException e2) {


                    } catch (IOException e1) {


                    }

                }

            }
        };
        mMap.setOnMapClickListener(list);

    }
}
