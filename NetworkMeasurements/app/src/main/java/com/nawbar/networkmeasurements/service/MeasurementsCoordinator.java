package com.nawbar.networkmeasurements.service;

import android.content.Context;
import android.util.Log;

import com.nawbar.networkmeasurements.measurements.LinkSource;
import com.nawbar.networkmeasurements.measurements.LocationSource;
import com.nawbar.networkmeasurements.measurements.RadioSource;
import com.nawbar.networkmeasurements.server_connection.Connection;
import com.nawbar.networkmeasurements.server_data.Link;
import com.nawbar.networkmeasurements.server_data.Location;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nawba on 15.10.2017.
 */

public class MeasurementsCoordinator implements LocationSource.Listener {

    private static final String TAG = MeasurementsCoordinator.class.getSimpleName();

    private static final int RADIO_MEAS_INTERVAL = 5000; // [ms]

    private static final int MIN_LOCATION_INTERVAL = 3000; // [ms]
    private static final int MAX_LOCATION_CHANGE = 5; // [m]

    private static final int LINK_UPDATE_INTERVAL = 2000; // [ms]

    private Connection connection;

    private ConsoleInput consoleInput;

    private boolean started;

    private RadioSource measurementsSource;
    private LocationSource locationSource;
    private LinkSource linkSource;

    private Timer radioTimer;
    private Timer linkTimer;

    public MeasurementsCoordinator(Context context, ConsoleInput console, Connection connection) {
        this.connection = connection;
        this.consoleInput = console;
        this.started = false;
        this.measurementsSource = new RadioSource(context, console);
        this.locationSource = new LocationSource(context, console, this,
                MIN_LOCATION_INTERVAL, MAX_LOCATION_CHANGE);
        this.linkSource = new LinkSource(console);
    }

    public void start() {
        locationSource.start();
        radioTimer = new Timer();
        radioTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendRadio();
            }
        }, 500, RADIO_MEAS_INTERVAL);
        linkTimer = new Timer();
        linkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendLink();
            }
        }, LINK_UPDATE_INTERVAL, LINK_UPDATE_INTERVAL);
        started = true;
        consoleInput.putMessage("SYS: measurements started");
    }

    public void shutdown() {
        locationSource.terminate();
        linkSource.terminate();
        radioTimer.cancel();
        radioTimer = null;
        linkTimer.cancel();
        linkTimer = null;
        started = false;
        consoleInput.putMessage("SYS: measurements ended");
    }

    @Override
    public void onFixed() {
        consoleInput.putMessage("LOC: fixed");
    }

    @Override
    public void onFixLost() {
        consoleInput.putMessage("LOC: fix lost");
    }

    @Override
    public void onLocationChanged(Location location) {
        consoleInput.putMessage("LOC: sending update");
        connection.sendLocation(location, new Connection.Listener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Location sent");
            }
            @Override
            public void onError(String message) {
                consoleInput.putMessage("ERR: While sending location: " + message);
            }
        });
    }

    private void sendRadio() {
        consoleInput.putMessage("NET: measuring eNBs");
        connection.sendRadio(measurementsSource.measure(), new Connection.Listener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Radio sent");
            }
            @Override
            public void onError(String message) {
                consoleInput.putMessage("ERR: While sending radio: " + message);
            }
        });
    }

    private void sendLink() {
        consoleInput.putMessage("LNK: capturing link state");
        connection.sendLink(linkSource.getActualLink(), new Connection.Listener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Link sent");
            }
            @Override
            public void onError(String message) {
                consoleInput.putMessage("ERR: While sending link: " + message);
            }
        });
    }

    public boolean isStarted() {
        return started;
    }
}
