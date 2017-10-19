package com.nawbar.networkmeasurements.measurements;

import android.os.AsyncTask;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Bartosz Nawrot on 2017-10-19.
 */

class LatencyTestTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = LatencyTestTask.class.getSimpleName();

    private final ConsoleInput console;

    private Process process;

    private double latency;
    private ReentrantLock lock = new ReentrantLock();

    LatencyTestTask(ConsoleInput console) {
        this.console = console;
    }

    void start() {
        execute();
    }

    void terminate() {
        if (process != null) {
            process.destroy();
        }
    }

    double getLatency() {
        lock.lock();
        double result = latency;
        lock.unlock();
        return result;
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.e(TAG, "Staring latency test task");
        try {
            process = Runtime.getRuntime().exec("/system/bin/ping onet.pl");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String inputLine = bufferedReader.readLine();
            while ((inputLine != null)) {
                if (inputLine.length() > 0 && inputLine.contains("avg")) {
                    break;
                } else {
                    parseLatency(inputLine);
                }
                inputLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseLatency(String input) {
        Log.e(TAG, "Latency input: " + input);
        if (input.contains("time=")) {
            String a = input.substring(input.indexOf("time=") + 5, input.indexOf(" ms"));
            double value = Double.valueOf(a);
            lock.lock();
            latency = value;
            lock.unlock();
        }
    }
}
