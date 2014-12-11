package com.example.androidexamples.maps;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.example.androidexamples.R;
import com.example.utils.Util;
import com.example.utils.location.LocationGetter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Map.java
 */

/**
 * @author Simone
 *         07/dic/2014
 */
public class MapActivityExample extends Activity implements OnMyLocationButtonClickListener,
        OnCheckedChangeListener {

    // STATIC ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    // private static final String NEWLINE = System.getProperty("line.separator");
    private static final int MIN_DIST_TO_DRAWN_POSITION = 5;// m
    private static final int MIN_ZOOM = 18;

    // ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private Resources res;
    private LocationManager locationManager;
    private LocationProvider locationProviderGPS;
    private LocationProvider locationProviderNETWORK;
    private MyLocationGetter locationGetter;
    private float totalDistance;
    // GUI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private GoogleMap map;
    private Polyline polyline;
    private CheckBox checkBox_trackMyPosition;
    private CheckBox checkBox_myDistance;
    private TextView textView_travelledDistance;
    private LinearLayout block_distance;
    private Marker marker_Start;
    private RelativeLayout.LayoutParams myLocationButtonLayoutParams;

    // private Marker markerCurrentLocation;

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        setContentView(R.layout.activity_map);

        res = getResources();
        checkBox_trackMyPosition =
                (CheckBox) findViewById(R.id.activity_map_checkBox_track_position);
        checkBox_myDistance = (CheckBox) findViewById(R.id.activity_map_checkBox_my_distance);
        checkBox_myDistance.setOnCheckedChangeListener(this);
        textView_travelledDistance = (TextView) findViewById(R.id.activity_map_textView_distance);
        block_distance = (LinearLayout) findViewById(R.id.activity_map_block_distance);
        map =
                ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.activity_map_fragment_map)).getMap();

        map.setMyLocationEnabled(true);// to enable myLocationButton layer
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setOnMyLocationButtonClickListener(this);

        // Get the button view
        View locationButton = findViewById(0x2);

        // and next place it, for exemple, on bottom right (as Google Maps app)
        myLocationButtonLayoutParams =
                (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        myLocationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        myLocationButtonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        myLocationButtonLayoutParams.setMargins(0, 0, 20, 60);

        // get the old registered location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Get the location provider
        locationProviderGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationProviderNETWORK = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (locationProviderGPS == null && locationProviderNETWORK == null) {
            new AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("No GPS or Network availability to define current location on map!")
                    .setNeutralButton(getResources().getString(R.string.ok), null);
        }
        else {
            if (!locationManager.isProviderEnabled(locationProviderGPS.getName())
                    && !locationManager.isProviderEnabled(locationProviderNETWORK.getName())) {
                new AlertDialog.Builder(this).setTitle("ERROR")
                        .setMessage("GPS and Network disabled")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton(getResources().getString(R.string.ok), null).show();

            }
            else if (!locationManager.isProviderEnabled(locationProviderGPS.getName())) {
                new AlertDialog.Builder(this).setTitle("ERROR").setMessage("GPS disabled")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton(getResources().getString(R.string.ok), null).show();
            }
            else if (!locationManager.isProviderEnabled(locationProviderNETWORK.getName())) {
                new AlertDialog.Builder(this).setTitle("ERROR").setMessage("Network disabled")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton(getResources().getString(R.string.ok), null).show();
            }

        }

        locationGetter =
                new MyLocationGetter(this, locationManager, 2, MIN_DIST_TO_DRAWN_POSITION,
                        locationProviderNETWORK, locationProviderGPS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {

        locationGetter.stopUpdatingLocation();
        super.onStop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMyLocationButtonClick() {

        Location location = locationGetter.getBetterLocation();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        zoomToLocation(latLng);
        return true;
    }

    // SUPPORT <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void zoomToLocation(LatLng latLng) {

        int currentZoom = (int) map.getCameraPosition().zoom;
        CameraUpdate cameraUpdate =
                CameraUpdateFactory.newLatLngZoom(latLng, currentZoom > MIN_ZOOM ? currentZoom
                        : MIN_ZOOM);
        map.animateCamera(cameraUpdate);
    }

    /**
     * 
     * @param distance
     * @return if <code>distance > 1000</code> <b>x km, y m</b>; otherwise <b>x m</b>
     */
    private String getFormattedDistance(float distance) {

        int km;
        int m;
        String toReturn;
        if (distance >= 1000) {
            km = (int) Math.floor(distance / 1000);
            m = (int) (distance % 1000);
            toReturn = km + " km, " + m + " m";
        }
        else
            toReturn = "" + (int) distance + " m";
        return toReturn;
    }

    // INNER CLASS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private class MyLocationGetter extends LocationGetter {

        private boolean firstLoad;

        /**
         * 
         * @param locationManager
         * @param minTime
         * @param minDistance
         * @param locationProviders
         */
        public MyLocationGetter(Context context, LocationManager locationManager, int minTime,
                int minDistance, LocationProvider... locationProviders) {

            super(locationManager, minTime, minDistance, locationProviders);
            firstLoad = true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onBetterLocationObtained(Location location) {

            LatLng newPoint = new LatLng(location.getLatitude(), location.getLongitude());

            Log.i("My Location", "New Point: Lon = " + newPoint.longitude + "; Lat = "
                    + newPoint.latitude);

            // Toast.makeText(
            // context,
            // "New position acquired from " + location.getProvider() + "." + NEWLINE
            // + " Location: Lon = " + location.getLongitude() + "; Lat = "
            // + location.getLatitude(), Toast.LENGTH_SHORT).show();
            // add a marker
            if (checkBox_myDistance.isChecked()) {// travelled distance
                LatLng lastPoint = polyline.getPoints().get(polyline.getPoints().size() - 1);
                float distance = Util.distance(lastPoint, newPoint);
                if (distance >= MIN_DIST_TO_DRAWN_POSITION) {
                    polyline.getPoints().add(newPoint);
                    totalDistance += distance;
                }
                String travelledDistance =
                        res.getString(R.string.travvelledDistance) + " "
                                + getFormattedDistance(totalDistance);
                textView_travelledDistance.setText(travelledDistance);
            }

            if (firstLoad || checkBox_trackMyPosition.isChecked())
                zoomToLocation(newPoint);
            firstLoad = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView.getId() == R.id.activity_map_checkBox_my_distance) {
            if (isChecked) {
                Location currentLocation = map.getMyLocation();
                LatLng currentPosition =
                        new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                marker_Start =
                        map.addMarker(new MarkerOptions()
                                .position(currentPosition)
                                .snippet(
                                        " Location: Lon = " + currentPosition.longitude
                                                + "; Lat = " + currentPosition.latitude)
                                .title("Start point"));
                if (block_distance.getVisibility() != View.VISIBLE)
                    block_distance.setVisibility(View.VISIBLE);
                polyline =
                        map.addPolyline(new PolylineOptions().color(color.holo_blue_bright).add(
                                currentPosition));
                textView_travelledDistance.setText(res.getString(R.string.travvelledDistance)
                        + " 0 m");
                myLocationButtonLayoutParams.setMargins(0, 0, 20, 60);
            }
            else {
                totalDistance = 0;
                marker_Start.remove();
                polyline.remove();
                block_distance.setVisibility(View.GONE);
                myLocationButtonLayoutParams.setMargins(0, 0, 20, 20);

            }
        }

    }
}
