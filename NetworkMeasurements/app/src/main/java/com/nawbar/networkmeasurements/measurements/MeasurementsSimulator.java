package com.nawbar.networkmeasurements.measurements;

import com.nawbar.networkmeasurements.server_connection.Connection;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bartosz Nawrot on 2017-11-02.
 */

public class MeasurementsSimulator extends IMeasurementsCoordinator {

    private static final String TAG = MeasurementsSimulator.class.getSimpleName();

    private Timer radioTimer;
    private Timer locationTimer;
    private Timer linkTimer;

    public MeasurementsSimulator(Connection connection, ConsoleInput console) {
        super(connection, console);
    }

    @Override
    public void start() {
        started = true;
        radioTimer = new Timer();
        radioTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                simulateRadio();
            }
        }, 300, RADIO_MEAS_INTERVAL);
        locationTimer = new Timer();
        locationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                simulateLocation();
            }
        }, 600, MIN_LOCATION_INTERVAL);
        linkTimer = new Timer();
        linkTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                simulateLink();
            }
        }, 900, LINK_UPDATE_INTERVAL);
    }

    @Override
    public void terminate() {
        started = false;
        radioTimer.cancel();
        radioTimer = null;
        locationTimer.cancel();
        locationTimer = null;
        linkTimer.cancel();
        linkTimer = null;
    }

    private void simulateRadio() {

    }

    private void simulateLocation() {
//        if (prevLocation != null) {
//            prevLocation.latitude += (Math.random() - 0.6) / 30000;
//            prevLocation.longitude += (Math.random() - 0.4) / 40000;
//        } else {
//            prevLocation = new Location(50.0, 20.0);
//        }
//        onLocationChanged(prevLocation);
    }

    private void simulateLink() {

    }
}
