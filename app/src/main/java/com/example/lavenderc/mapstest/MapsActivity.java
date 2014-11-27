package com.example.lavenderc.mapstest;

import android.graphics.Color;
import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import android.view.View;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private List<Address> addresses = null;
    private CharSequence text;
    private boolean isJsonShown = false;
    private Circle circle;
    private ArrayList<Circle> circles = new ArrayList<Circle>();
    private ArrayList<Object> layer = new ArrayList<Object>();
    private Polygon p;

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

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setJSONLayer();
            }
        }
    }


    public void setJSONLayer() {
        try {
            InputStream inputStream = getAssets().open("text.json");
            int sizeOfJSONFile = inputStream.available();
            byte[] bytes = new byte[sizeOfJSONFile];
            inputStream.read(bytes);
            String result = new String(bytes, "UTF-8");
            JSONObject text = new JSONObject(result);
            String fillColour = text.getJSONObject("properties").get("color").toString();
            System.out.println(fillColour);
            JSONArray a = text.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
            PolygonOptions poly = new PolygonOptions();

            for (int i = 0; i < a.length(); i++) {
                String lat = null;
                String lon = null;
                lat = a.getJSONArray(i).get(0).toString().trim();
                lon = a.getJSONArray(i).get(1).toString().trim();
                poly.add(new LatLng(Double.parseDouble(lon), Double.parseDouble(lat)));
            }
            poly.fillColor(Color.parseColor(fillColour));
            p = mMap.addPolygon(poly);
            inputStream.close();
        } catch (Exception e) {
            System.out.println("Nope");
        }
    }



    public void buttonAction (View view) {
        isJsonShown = !isJsonShown;
        p.setVisible(isJsonShown);
        System.out.println(isJsonShown);
    }
}
