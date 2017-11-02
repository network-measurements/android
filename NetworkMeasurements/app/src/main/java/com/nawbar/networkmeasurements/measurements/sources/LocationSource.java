package com.nawbar.networkmeasurements.measurements.sources;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class LocationSource implements LocationListener {

    private static final String TAG = LocationSource.class.getSimpleName();

    private final int minInterval;
    private final int maxDistance;

    private Context context;
    private ConsoleInput consoleInput;

    private LocationManager locationManager;

    private Listener listener;

    public LocationSource(Context context, ConsoleInput console, Listener listener,
                          int minInterval, int maxDistance) {
        this.minInterval = minInterval;
        this.maxDistance = maxDistance;
        this.context = context;
        this.consoleInput = console;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.listener = listener;
    }

    public void start() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO handle this case somehow, permissions check is preformed on app startup
            Log.e(TAG, "Error, no permissions for localization");
            consoleInput.putMessage("ERR: No permissions for localization");
        } else {
            Log.e(TAG, "Starting localization, interval: " + minInterval + "ms, distance: " + maxDistance + "m");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minInterval, maxDistance, this);
        }
    }

    public void terminate() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location.toString());
        listener.onLocationChanged(new com.nawbar.networkmeasurements.measurements.models.Location(location));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled");
    }

    public interface Listener {
        void onFixed();
        void onFixLost();
        void onLocationChanged(com.nawbar.networkmeasurements.measurements.models.Location location);
    }
}
