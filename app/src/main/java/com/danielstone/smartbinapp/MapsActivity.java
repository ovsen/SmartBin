package com.danielstone.smartbinapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    CoordinatorLayout coordinatorLayout;
    LocationManager locationManager;
    String provider;
    Location currentLocation;
    Location finalLocation;
    boolean mapReady = false;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result ="";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("MapsActivity", "Data abt to be proccessed");

            try {
                /*
                JSONObject geoJsonData = new JSONObject(result);

                JSONArray routesJSON = geoJsonData.getJSONArray("routes");

                JSONObject zeroJSON = routesJSON.getJSONObject(0);

                JSONObject overviewJSON = zeroJSON.getJSONObject("overview_polyline");

                String LINE = overviewJSON.getString("points");

                Log.i("MapsActivity", LINE);

                */

                if(!(result.isEmpty())) {

                    Log.i("Maps", result);

                    String LINE = result;

                    List<LatLng> decodedPath = PolyUtil.decode(LINE);

                    mMap.addPolyline(new PolylineOptions().addAll(decodedPath)
                            .width(10)
                            .color(R.color.routeColor));

                    Snackbar.make(coordinatorLayout, "Route Loaded!", Snackbar.LENGTH_SHORT);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_maps_activity_coordinator);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);

        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mapReady = true;
    }

    public void getRoute(){

        Snackbar.make(coordinatorLayout, "Loading route...", Snackbar.LENGTH_LONG);

        //final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=";
        final String BASE_URL = "http://daniel-stone.uk/smartbin/fetch-route.php?current=";
        final String CURRENT_LOCATION_LAT_LNG = String.valueOf(finalLocation.getLatitude()) + "+" + String.valueOf(finalLocation.getLongitude());
        //final String DESTINATION_BASE = "&destination=";
        //final String DESTINATION_LAT_LNG = CURRENT_LOCATION_LAT_LNG;
        //final String WAYPOINT_BASE = "&waypoints=optimize:true";
        final String WAYPOINT_BASE = "&waypoints=";
        String WAYPOINTS = "";

        for (int i = 0; i < FetchDataActivity.fullBinIDs.size(); i++) {
            WAYPOINTS = WAYPOINTS + "|" + FetchDataActivity.latArray.get(i).toString() + "+" + FetchDataActivity.lngArray.get(i).toString();
            //Log.i("MapsActivity", WAYPOINTS);
        }

        //Log.i("MapsActivity", String.valueOf(FetchDataActivity.fullBinIDs.size()));

        //final String API_KEY_BASE = "&key=";
        final String API_KEY_BASE = "&apikey=";
        final String API_KEY = "AIzaSyCG3V-Me1cMSaTRX8M7KFT6cMUEsukZQPA";

        //final String FINAL_URL = BASE_URL + CURRENT_LOCATION_LAT_LNG + DESTINATION_BASE + DESTINATION_LAT_LNG + WAYPOINT_BASE + WAYPOINTS + API_KEY_BASE + API_KEY;
        final String FINAL_URL = BASE_URL + CURRENT_LOCATION_LAT_LNG + WAYPOINT_BASE + WAYPOINTS + API_KEY_BASE + API_KEY;

        Log.i("MapsActivity", FINAL_URL);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(FINAL_URL);
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mapReady && finalLocation == null) {

            finalLocation = location;

            getRoute();

            /*
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();

            Log.i("Location", "Latitude: " + lat.toString());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Your location")).showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16f));

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                List<Address> addressList = geocoder.getFromLocation(lat, lng, 1);

                if (addressList != null && addressList.size() > 0) {

                    Log.i("Location Address", addressList.get(0).toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
