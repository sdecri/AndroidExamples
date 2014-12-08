/**
 * LocationUtils.java
 */
package com.example.utils.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Simone
 *         07/dic/2014
 */
public abstract class LocationGetter {

    // ATTRIBUTES <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private LocationManager locationManager;
    private MyLocationProvider[] locationProviders;
    /**
     * Time interval among older and newer {@link Location}.
     * Used in method {@link LocationGetter#getBetterLocation(Location, Location)}
     */
    private int significatTimeInterval = 1000 * 60 * 2;// 2 min
    /**
     * Accurancy difference among older and newer {@link Location}.
     * Used in method {@link LocationGetter#getBetterLocation(Location, Location)}
     */
    private int significatAccurancyDelta = 200;
    private Location betterLocation;
    // CONSTRUCTORS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * 
     * @param locationManager
     * @param minTime
     *            minimum time in seconds to update position
     * @param minDistance
     *            minimum distance in meters to update position
     * @param locationProviders
     */
    public LocationGetter(LocationManager locationManager, int minTime, int minDistance,
            LocationProvider...locationProviders) {

        super();
        this.locationManager = locationManager;
        if (locationProviders != null) {
            this.locationProviders = new MyLocationProvider[locationProviders.length];
            for (int i = 0; i < locationProviders.length; i++) {
                this.locationProviders[i] = new MyLocationProvider(locationProviders[i]);
                locationManager.requestLocationUpdates(
                        this.locationProviders[i].locationProvider.getName(), (long) minTime,
                        (float) minDistance, this.locationProviders[i]);
                
                // Get the last known location
                Location lastKnownLocation=locationManager.getLastKnownLocation(locationProviders[i].getName());
                if(isBetterLocation(lastKnownLocation))
                        betterLocation=lastKnownLocation;
            }
        }
    }

    // METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    
    /**
     * Determines whether one Location reading is better than the current Location fix
     * 
     * @param locationNew
     *            The new Location that you want to evaluate
     */
    public synchronized boolean isBetterLocation(Location locationNew) {

        if(locationNew==null)
            return false;
        if (betterLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = locationNew.getTime() - betterLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > significatTimeInterval;
        boolean isSignificantlyOlder = timeDelta < -significatTimeInterval;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        }
        else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (locationNew.getAccuracy() - betterLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > significatAccurancyDelta;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider =
                isSameProvider(locationNew.getProvider(), betterLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        }
        else if (isNewer && !isLessAccurate) {
            return true;
        }
        else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {

        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    /**
     * Stop to update {@link Location}
     */
    public void stopUpdatingLocation(){
        for (MyLocationProvider myLocationProvider : locationProviders) {
            locationManager.removeUpdates(myLocationProvider);
        }
    }
    
    // GET & SET <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    
    /**
     * 
     * @return the current better {@link Location}
     */
    public final synchronized Location getBetterLocation(){
        return betterLocation;
    }
    
    /**
     * 
     * @param location
     */
    private synchronized void setBetterLocation(Location location){
        betterLocation=location;
    }
    
    /**
     * @param significatTimeInterval
     *            the significatTimeInterval to set
     */
    public void setSignificatTimeInterval(int significatTimeInterval) {

        this.significatTimeInterval = significatTimeInterval;
    }

    /**
     * @param significatAccurancyDelta
     *            the significatAccurancyDelta to set
     */
    public void setSignificatAccurancyDelta(int significatAccurancyDelta) {

        this.significatAccurancyDelta = significatAccurancyDelta;
    }

    // ABSTRACT METHODS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    /**
     * Fired when a new better {@link Location} has been acquired.
     * To change the default method used to check if a {@link Location} is better then the older, the method
     * 
     * @param location
     */
    public abstract void onBetterLocationObtained(Location location);

    // INNER CLASS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private final class MyLocationProvider implements LocationListener {

        private LocationProvider locationProvider;

        /**
         * @param locationProvider
         * @param status
         */
        public MyLocationProvider(LocationProvider locationProvider) {

            this.locationProvider = locationProvider;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onLocationChanged(Location location) {
            Log.i("Provider: "+location.getProvider(),"Event: Location changed");
            if(isBetterLocation(location)){
                setBetterLocation(location);
                // run the callback
                onBetterLocationObtained(location);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("Provider: "+provider,"Event: Status changed --> " + status);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onProviderEnabled(String provider) {
            Log.i("Provider: "+provider,"Event: Provider enabled");
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onProviderDisabled(String provider) {
            Log.w("Provider: "+provider,"Event: Provider disabled");
        }
    
    }


}
