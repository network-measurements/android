package com.nawbar.networkmeasurements.measurements;

import com.nawbar.networkmeasurements.measurements.models.CellData;
import com.nawbar.networkmeasurements.measurements.models.Link;
import com.nawbar.networkmeasurements.measurements.models.Location;
import com.nawbar.networkmeasurements.measurements.models.Radio;
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

    private long counter;

    private Location location;
    private Link link;

    public MeasurementsSimulator(Connection connection, ConsoleInput console) {
        super(connection, console);
        this.location = new Location(50.0216254, 19.8910485);
        this.link = new Link();
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
        counter = 0;
        console.putMessage("SYS: Started simulator");
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
        console.putMessage("SYS: Simulator terminated");
    }

    private boolean switched = false;

    private void simulateRadio() {
        // simulate radio measurement
        Radio radio = new Radio();
        int cellId = 1700494;
        int dbm = (int) (-92 - Math.random() * 12);
        if (counter > 12) {
            if (!switched) {
                console.putMessage("NET: Cell switched");
                switched = true;
            }
            cellId = 1700496;
            dbm = (int) (-74 - Math.random() * 8);
        }
        CellData cell = new CellData(CellData.CellType.LTE, 6, 260,
                cellId, 206, dbm);
        radio.addCell(cell);

        sendRadio(radio);
    }

    private void simulateLocation() {
        // simulate device movement
        location.latitude += (0.00002 + randN(0.00009) + Math.sin(counter*0.04)/3400);
        location.longitude -= (0.00002 + randN(0.00007) + Math.cos(counter*0.02)/3000);
        ++counter;

        sendLocation(location);
    }

    private void simulateLink() {
        // simulate link measurement
        link.downLink = 5000000 + Math.random() * 100000
                + Math.sin(counter / 2.0) * 500000
                + Math.sin(counter / 5.0) * 100000;
        link.upLink = 3700000 + Math.random() * 100000
                + Math.sin(counter / 5.0) * 100000
                + Math.sin(counter / 4.0) * 800000;
        link.latency = 46 + Math.random() * 10
                + Math.sin(counter / 3.0) * 5
                + Math.sin(counter / 1.0) * 5;

        sendLink(link);
    }

    private double randN(final double bound) {
        return (Math.random() - 0.5) * 2 * bound;
    }
}
