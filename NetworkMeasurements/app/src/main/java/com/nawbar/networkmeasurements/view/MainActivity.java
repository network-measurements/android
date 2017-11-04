package com.nawbar.networkmeasurements.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nawbar.networkmeasurements.R;
import com.nawbar.networkmeasurements.logger.Logger;
import com.nawbar.networkmeasurements.measurements.MeasurementsSimulator;
import com.nawbar.networkmeasurements.server_connection.Connection;
import com.nawbar.networkmeasurements.measurements.IMeasurementsCoordinator;
import com.nawbar.networkmeasurements.measurements.MeasurementsCoordinator;
import com.nawbar.networkmeasurements.service.MeasurementsService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ConsoleInput {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSION = 1;

    private ListView console;
    private ArrayAdapter consoleAdapter;

    private Connection connection;
    private IMeasurementsCoordinator coordinator;

    private boolean connecting;

    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        console = (ListView) findViewById(R.id.console);
        List<String> consoleList = new ArrayList<>();
        consoleAdapter = new ArrayAdapter<>(this,
                R.layout.console_row, R.id.text_row, consoleList);
        console.setAdapter(consoleAdapter);

        logger = new Logger(this, this);

        connecting = false;

        connection = new Connection(this, this, logger);
        coordinator = new MeasurementsCoordinator(this, connection, this);
        //coordinator = new MeasurementsSimulator(connection, this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(android.R.drawable.ic_dialog_map);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coordinator.isStarted()) {
                    endMeasurements(fab);
                } else {
                    startMeasurements(fab);
                }
//                if (coordinator.isStarted()) {
//                    coordinator.terminate();
//                } else {
//                    coordinator.start();
//                }
            }
        });

        if (coordinator instanceof MeasurementsSimulator) {
            putConsoleMessage("SYS: ==== SIMULATOR MODE ====");
        }

        checkPermissions();

        Log.e(TAG, "onCreate done");
    }

    private void startMeasurementService() {
        startService(new Intent(this, MeasurementsService.class));
    }

    private void startMeasurements(final FloatingActionButton fab) {
        if (!connecting) {
            Log.e(TAG, "Starting measurements session");
            connecting = true;
            fab.setImageResource(android.R.drawable.ic_dialog_info);
            connection.startSession(new Connection.Listener() {
                @Override
                public void onSuccess() {
                    connecting = false;
                    coordinator.start();
                    fab.setImageResource(android.R.drawable.ic_dialog_alert);
                }

                @Override
                public void onError(String message) {
                    connecting = false;
                    fab.setImageResource(android.R.drawable.ic_dialog_map);
                    putConsoleMessage("ERR: while starting session: " + message);
                }
            });
        } else {
            Log.e(TAG, "Skipping connect, already processing...");
        }
    }

    private void endMeasurements(final FloatingActionButton fab) {
        Log.e(TAG, "Shouting down measurements session");
        coordinator.terminate();
        fab.setImageResource(android.R.drawable.ic_dialog_map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coordinator.terminate();
        logger.close();
    }

    public void putConsoleMessage(final String message) {
        console.post(new Runnable() {
            @Override
            public void run() {
                consoleAdapter.add(message);
                console.setSelection(consoleAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void putMessage(String message) {
        putConsoleMessage(message);
    }

    public void checkPermissions() {
        int permissionCoarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionFile = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCoarse != PackageManager.PERMISSION_GRANTED
                || permissionFine != PackageManager.PERMISSION_GRANTED
                || permissionFile != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "requestPermissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            putConsoleMessage("SYS: Permissions check successful");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if ((grantResults.length > 1)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        && (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e(TAG, "Permissions granted");
                    putConsoleMessage("SYS: Permissions granted");
                } else {
                    putConsoleMessage("ERR: Can not work without permissions");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clean_console) {
            consoleAdapter.clear();
            Log.e(TAG, "Console cleaned");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
