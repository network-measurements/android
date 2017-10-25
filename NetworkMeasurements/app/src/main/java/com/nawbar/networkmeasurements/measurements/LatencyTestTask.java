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

    private static final int HISTORY_LENGTH = 3;

    private final ConsoleInput console;

    private Process process;
    private Timer timer;
    private BufferedReader bufferedReader;

    private double history[];
    private int historyPosition;

    LatencyTestTask(ConsoleInput console) {
        this.console = console;
        initializeHistory();
    }

    void start() {
        try {
            initializeHistory();
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
        double result = 0;
        int n = 0;
        for (double v : history) {
            if (v != -1.) {
                result += v;
                ++n;
            }
        }
        if (n > 0) return result / n;
        else return 0;
    }

    private void initializeHistory() {
        history = new double[HISTORY_LENGTH];
        for (int i = 0; i < HISTORY_LENGTH; ++i) {
            history[i] = -1.;
        }
        historyPosition = 0;
    }

    private void parseLatency(String input) {
        Log.e(TAG, "Latency input: " + input);
        if (input.contains("time=")) {
            String a = input.substring(input.indexOf("time=") + 5, input.indexOf(" ms"));
            history[historyPosition] = Double.valueOf(a);
            historyPosition++;
            if (historyPosition >= HISTORY_LENGTH) historyPosition = 0;
        }
    }
}
