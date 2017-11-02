package com.nawbar.networkmeasurements.measurements;

import android.content.Context;

import com.nawbar.networkmeasurements.measurements.sources.LinkSource;
import com.nawbar.networkmeasurements.measurements.sources.LocationSource;
import com.nawbar.networkmeasurements.measurements.sources.RadioSource;
import com.nawbar.networkmeasurements.server_connection.Connection;
import com.nawbar.networkmeasurements.measurements.models.Location;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nawba on 15.10.2017.
 */

public class MeasurementsCoordinator extends IMeasurementsCoordinator
        implements LocationSource.Listener {

    private static final String TAG = MeasurementsCoordinator.class.getSimpleName();

    private RadioSource measurementsSource;
    private LocationSource locationSource;
    private LinkSource linkSource;

    private Timer radioTimer;
    private Timer linkTimer;

    public MeasurementsCoordinator(Context context, Connection connection, ConsoleInput console) {
        super(connection, console);
        this.measurementsSource = new RadioSource(context, console);
        this.locationSource = new LocationSource(context, console, this,
                MIN_LOCATION_INTERVAL, MAX_LOCATION_CHANGE);
        this.linkSource = new LinkSource(console);
    }

    @Override
    public void start() {
        locationSource.start();
        linkSource.start();
        radioTimer = new Timer();
        radioTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                measureRadio();
            }
        }, 500, RADIO_MEAS_INTERVAL);
        linkTimer = new Timer();
        linkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                measureLink();
            }
        }, LINK_UPDATE_INTERVAL, LINK_UPDATE_INTERVAL);
        started = true;
        console.putMessage("SYS: measurements started, session ID: "
                + connection.getSessionId());
    }

    @Override
    public void terminate() {
        locationSource.terminate();
        linkSource.terminate();
        radioTimer.cancel();
        radioTimer = null;
        linkTimer.cancel();
        linkTimer = null;
        started = false;
        console.putMessage("SYS: measurements ended");
    }

    @Override
    public void onFixed() {
        console.putMessage("LOC: fixed");
    }

    @Override
    public void onFixLost() {
        console.putMessage("LOC: fix lost");
    }

    @Override
    public void onLocationChanged(Location location) {
        sendLocation(location);
    }

    private void measureRadio() {
        sendRadio(measurementsSource.measure());
    }

    private void measureLink() {
        sendLink(linkSource.getActualLink());
    }
}
