package com.example.barick.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Provider;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MapsActivity.this,
                        ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("TEST"));
                String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                        Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()) +
                        "&radius=1000&type=food&keyword=&key=AIzaSyCRwOSQBwuDEkWiE0gMdHh7QR8HIMIFlsE";
                new DownloaderTask().execute(URL);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        URLConnection connection = new URL(url).openConnection();
        InputStream inputStream = connection.getInputStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            inputStream.close();
        }
    }

    class DownloaderTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urls) {
            try {
                return readJsonFromUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                JSONArray array = jsonObject.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    String restaurantName = (String) array.getJSONObject(i).getString("name");
                    Double latitudeThing = (Double) array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lat");
                    Double longitudeThing = (Double) array.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").get("lng");

                    mMap.addMarker(new MarkerOptions().position(new LatLng(latitudeThing, longitudeThing)).title(restaurantName));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}