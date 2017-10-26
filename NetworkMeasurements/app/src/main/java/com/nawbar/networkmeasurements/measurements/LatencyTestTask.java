package com.nawbar.networkmeasurements.measurements;

import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Bartosz Nawrot on 2017-10-19.
 */

class LatencyTestTask {

    private static final String TAG = LatencyTestTask.class.getSimpleName();

    private final ConsoleInput console;

    private Process process;
    private Timer timer;
    private BufferedReader bufferedReader;

    private double latency;

    LatencyTestTask(ConsoleInput console) {
        this.console = console;
        this.latency = 0.0;
    }

    void start() {
        try {
            latency = 0.0;
            process = Runtime.getRuntime().exec("/system/bin/ping google.pl");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        while (bufferedReader.ready()) {
                            String inputLine = bufferedReader.readLine();
                            if (inputLine != null) {
                                if (inputLine.length() > 0 && inputLine.contains("avg")) {
                                    timer.cancel();
                                    break;
                                } else {
                                    parseLatency(inputLine);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 300, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void terminate() {
        if (process != null) {
            process.destroy();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    double getLatency() {
        return latency;
    }

    private void parseLatency(String input) {
        Log.e(TAG, "Latency input: " + input);
        if (input.contains("time=")) {
            String a = input.substring(input.indexOf("time=") + 5, input.indexOf(" ms"));
            latency = Double.valueOf(a);
        }
    }
}
