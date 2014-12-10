package com.example.androidexamples.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.androidexamples.R;
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
public class MapActivityExample extends Activity implements OnMyLocationButtonClickListener {

    private static final String NEWLINE = System.getProperty("line.separator");

    // ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private GoogleMap map;
    private LocationManager locationManager;
    private LocationProvider locationProviderGPS;
    private LocationProvider locationProviderNETWORK;
    private MyLocationGetter locationGetter;
    private Polyline polyline;
    private LatLng polyLastPoint;

    // private Marker markerCurrentLocation;

    // GUI <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle arg0) {

        super.onCreate(arg0);

        setContentView(R.layout.activity_map);

        map =
                ((MapFragment) getFragmentManager()
                        .findFragmentById(R.id.activity_map_fragment_map)).getMap();

        map.setMyLocationEnabled(true);// to enable myLocationButton layer
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setOnMyLocationButtonClickListener(this);

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
                new MyLocationGetter(this, locationManager, 2, 0, locationProviderNETWORK,
                        locationProviderGPS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {

        // locationGetter.stopUpdatingLocation();
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

    private void zoomToLocation(LatLng latLng) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        map.animateCamera(cameraUpdate);
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

            // Toast.makeText(
            // context,
            // "New position acquired from " + location.getProvider() + "." + NEWLINE
            // + " Location: Lon = " + location.getLongitude() + "; Lat = "
            // + location.getLatitude(), Toast.LENGTH_SHORT).show();
            // add a marker
            if(true){//track position
                //todo_here
            }else{
                
            }
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (firstLoad) {
                // markerCurrentLocation =
                // map.addMarker(new MarkerOptions().position(latLng)
                // .title("Point from: " + location.getProvider())
                // .snippet(latLng.longitude + ", " + latLng.latitude));
                zoomToLocation(latLng);
            }
            else {
                // markerCurrentLocation.setTitle("Point from: " + location.getProvider());
                // markerCurrentLocation.setPosition(latLng);
            }
            polyline =
                    map.addPolyline(new PolylineOptions().width(20).add(new LatLng(0, 0))
                            .add(new LatLng(latLng.latitude, latLng.longitude)));

            firstLoad = false;
        }

    }

}
