package com.example.lavenderc.mapstest;

import android.graphics.Color;
import android.sax.Element;
import android.support.annotation.XmlRes;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.view.View;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


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

        InputStream input;
        int size;
        byte[] bytes;
        PolylineOptions line = new PolylineOptions();
        HashMap<String, String> hash = new HashMap<String, String>();
        String colour = "";
        String hex = "";
        String currentColour = "#blueLine";

        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream stream = getApplicationContext().getAssets().open("cta.kml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.equals("coordinates")) {
                        String[] l = parser.nextText().split("\n");
                        for (String s: l) {
                            String[] p = s.split(",");
                            if (p.length > 2) {
                                line.add(new LatLng(Double.parseDouble(p[1]), Double.parseDouble(p[0])))
                                        .width(20)
                                        .color(Color.parseColor("#" + hash.get(currentColour)));

                            }
                        }
                    } else if (name.equals("Style")) {
                        colour = parser.getAttributeValue(null, "id");
                    } else if (name.equals ("color")) {
                        hex = parser.nextText();
                        hash.put("#"+colour, hex);
                    } else if (name.equals("styleUrl")) {
                        currentColour = parser.nextText();

                    }
                }
                eventType = parser.next();
                mMap.addPolyline(line);
                line = new PolylineOptions();
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
