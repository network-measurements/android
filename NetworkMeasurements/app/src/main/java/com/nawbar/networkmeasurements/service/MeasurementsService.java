package com.nawbar.networkmeasurements.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.nawbar.networkmeasurements.logger.Logger;
import com.nawbar.networkmeasurements.measurements.MeasurementsCoordinator;
import com.nawbar.networkmeasurements.server_connection.Connection;
import com.nawbar.networkmeasurements.view.ConsoleInput;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class MeasurementsService extends Service {

    private static final String TAG = MeasurementsService.class.getSimpleName();

    private Listener listener;
    private ConsoleInput console;

    private Connection connection;
    private MeasurementsCoordinator coordinator;

    private Logger logger;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        connection = new Connection(this, console, logger);
        coordinator = new MeasurementsCoordinator(this, connection, console);

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    private void startMeasurements() {
        Log.e(TAG, "Starting down measurements session");
        listener.onSessionStarting();
        connection.startSession(new Connection.Listener() {
            @Override
            public void onSuccess() {
                coordinator.start();
                listener.onMeasurementsStarted();
            }
            @Override
            public void onError(String message) {
                listener.onMeasurementsShutdown();
                console.putMessage("ERR: while starting session: " + message);
            }
        });
    }

    private void endMeasurements() {
        Log.e(TAG, "Shouting measurements session");
        coordinator.terminate();
        listener.onMeasurementsShutdown();
    }

    public interface Listener {
        void onSessionStarting();
        void onMeasurementsStarted();
        void onMeasurementsShutdown();
    }
}