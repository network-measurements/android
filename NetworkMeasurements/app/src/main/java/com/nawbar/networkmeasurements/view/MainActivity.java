package com.nawbar.networkmeasurements.view;

import android.Manifest;
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
import com.nawbar.networkmeasurements.measurements.LocationSource;
import com.nawbar.networkmeasurements.measurements.RadioSource;
import com.nawbar.networkmeasurements.server_connection.Connection;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSION = 1;

    private ListView console;
    private ArrayAdapter consoleAdapter;

    private Connection connection;
    private RadioSource measurementsSource;
    private LocationSource locationSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConsoleInput consoleInput = new ConsoleInput() {
            @Override
            public void putMessage(String message) {
                putConsoleMessage(message);
            }
        };

        console = (ListView) findViewById(R.id.console);
        List<String> consoleList = new ArrayList<>();
        consoleAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, consoleList);
        console.setAdapter(consoleAdapter);

        connection = new Connection(this, consoleInput);
        measurementsSource = new RadioSource(this, consoleInput);
        locationSource = new LocationSource(this, consoleInput);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startMeasurement();
                //connection.startSession();
                locationSource.startLocalization();
            }
        });

        Log.e(TAG, "onCreate done");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationSource.endLocalization();
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

    public void startMeasurement() {
        Log.e(TAG, "startMeasurement");
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "requestPermissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        } else {
            measurementsSource.measure();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult");
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e(TAG, "PERMISSION_GRANTED");
                    measurementsSource.measure();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
