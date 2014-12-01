package com.example.lavenderc.mapstest;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean isJsonShown = false;
    private ArrayList<Polygon> layer = new ArrayList<Polygon>();
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
                //setJSONLayer();
            }
        }
    }

    public void setKML () {

        PolylineOptions options = new PolylineOptions();
        HashMap<String, String> colourPalette = new HashMap<String, String>();
        HashMap<String, Integer> lineWidths = new HashMap<String, Integer>();
        Integer lon = 0, lat = 1;
        Double lonDouble, latDouble;
        LatLng latLng;

        String name = new String();
        String colourID = new String();
        String currentColour = new String();

        String[] lines;

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            InputStream stream = getApplicationContext().getAssets().open("cta.kml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    name = parser.getName();
                    if (name.equals("coordinates")) {
                        lines = parser.nextText().split("\n");
                        for (String point: lines) {
                            String[] coordinate = point.split(",");
                            if (coordinate.length > 2) {
                                latDouble = Double.parseDouble(coordinate[lat]);
                                lonDouble = Double.parseDouble(coordinate[lon]);
                                latLng = new LatLng(latDouble,lonDouble);
                                options.add(latLng)
                                       .width(lineWidths.get("#"+colourID))
                                       .color(Color.parseColor("#" + colourPalette.get(currentColour)));
                            }
                        }
                    } else if (name.equals("Style")) {
                        colourID = parser.getAttributeValue(null, "id");
                    } else if (name.equals ("color")) {
                        colourPalette.put("#" + colourID, parser.nextText());
                    } else if (name.equals("width")) {
                        lineWidths.put("#" + colourID, Integer.parseInt(parser.nextText()));

                    }else if (name.equals("styleUrl")) {
                        currentColour = parser.nextText();
                    }
                }
                eventType = parser.next();
                mMap.addPolyline(options);
                options = new PolylineOptions();
            }
        } catch (Exception e) {
            System.out.println("Nope");
        }
    }


    public void setJSONLayer () {
        JSONArray coordinates;
        JSONArray points;
        JSONArray features;
        JSONObject geometry;
        InputStream input;
        int size;
        byte[] bytes;

        try {
            input = getAssets().open("google.json");
            size = input.available();
            bytes = new byte[size];
            input.read(bytes);
            JSONObject text = new JSONObject(new String(bytes, "UTF-8"));

            features = text.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {

                geometry = features.getJSONObject(i).getJSONObject("geometry");
                String fillColour = features.getJSONObject(i).getJSONObject("properties").get("color").toString();
                coordinates = geometry.getJSONArray("coordinates");

                for (int k = 0; k < coordinates.length(); k++) {
                    PolygonOptions poly = new PolygonOptions();
                    points = coordinates.getJSONArray(k);
                    for (int j = 0; j < points.length(); j++) {
                        String lat = points.getJSONArray(j).get(0).toString();
                        String lon =  points.getJSONArray(j).get(1).toString();
                        poly.add(new LatLng(Double.parseDouble(lon), Double.parseDouble(lat)));
                    }
                    poly.fillColor(Color.parseColor(fillColour));
                    p = mMap.addPolygon(poly);
                    layer.add(p);
                }
            }

        } catch (Exception e)  {


        }


    }






    public void buttonAction (View view) {
        setKML();
        isJsonShown = !isJsonShown;
        for (Polygon poly: layer) {
            poly.setVisible(isJsonShown);
        }
    }
}
