package com.nawbar.networkmeasurements.measurements;

import android.util.Log;

import com.nawbar.networkmeasurements.measurements.models.Link;
import com.nawbar.networkmeasurements.measurements.models.Location;
import com.nawbar.networkmeasurements.measurements.models.Radio;
import com.nawbar.networkmeasurements.server_connection.Connection;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.util.Locale;

/**
 * Created by Bartosz Nawrot on 2017-11-02.
 */

public abstract class IMeasurementsCoordinator {

    private static final String TAG = MeasurementsSimulator.class.getSimpleName();

    static final int RADIO_MEAS_INTERVAL = 5000; // [ms]

    static final int MIN_LOCATION_INTERVAL = 3000; // [ms]
    static final int MAX_LOCATION_CHANGE = 5; // [m]

    static final int LINK_UPDATE_INTERVAL = 2000; // [ms]

    Connection connection;
    ConsoleInput console;

    boolean started;

    IMeasurementsCoordinator(Connection connection, ConsoleInput console) {
        this.connection = connection;
        this.console = console;
        this.started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public abstract void start();
    public abstract void terminate();

    void sendRadio(final Radio radio) {
        console.putMessage("NET: " + radio.getShortString());
        connection.sendRadio(radio, new Connection.Listener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Radio sent");
            }
            @Override
            public void onError(String message) {
                console.putMessage("ERR: While sending radio: " + message);
            }
        });
    }

    void sendLocation(final Location location) {
        console.putMessage("LOC: [" + String.format(Locale.ENGLISH, "%.5f", location.latitude) +
                ", " + String.format(Locale.ENGLISH, "%.5f", location.longitude) + "]");
        connection.sendLocation(location, new Connection.Listener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Location sent");
            }
            @Override
            public void onError(String message) {
                console.putMessage("ERR: While sending location: " + message);
            }
        });
    }

    void sendLink(final Link link) {
        console.putMessage("LNK: D: " + String.format(Locale.ENGLISH, "%.2f", link.getDownLinkInMbps())
                + " Mb/s, U: " + String.format(Locale.ENGLISH, "%.2f", link.getUpLinkInMbps()) + " Mb/s"
                + " L: " + String.format(Locale.ENGLISH, "%.1f", link.latency) + " ms");
        connection.sendLink(link, new Connection.Listener() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "Link sent");
            }
            @Override
            public void onError(String message) {
                console.putMessage("ERR: While sending link: " + message);
            }
        });
    }
}
