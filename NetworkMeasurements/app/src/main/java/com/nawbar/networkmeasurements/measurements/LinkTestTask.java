package com.nawbar.networkmeasurements.measurements;

import android.os.AsyncTask;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

/**
 * Created by nawba on 17.10.2017.
 * https://github.com/bertrandmartel/speed-test-lib
 */

class LinkTestTask implements ISpeedTestListener {

    private static final String TAG = LinkSource.class.getSimpleName();

    private static final int START_DELAY = 500;
    private static final int UPDATE_INTERVAL = 1000;
    private static final int HISTORY_LENGTH = 3;

    enum Type {
        DOWN,
        UP
    }

    private final ConsoleInput console;
    private final String name;

    private final Type type;

    private AsyncTask<Void, Void, String> task;
    private SpeedTestSocket socket;

    private double history[];
    private int historyPosition;
    private Lock lock = new ReentrantLock();

    LinkTestTask(ConsoleInput console, Type type) {
        this.console = console;
        this.name = "[" + type.toString() + "]";
        this.type = type;
        this.socket = new SpeedTestSocket();
        this.socket.addSpeedTestListener(this);
        initializeHistory();
    }

    void start() {
        initializeHistory();
        startTask();
    }

    void terminate() {
        if (task != null) task.cancel(true);
        socket.closeSocket();
    }

    double getRate() {
        return computeDownLink();
    }

    private void startTask() {
        task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.e(TAG, "Starting " + name + " link test with delay: " + START_DELAY + "ms");
                try {
                    Thread.sleep(START_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!task.isCancelled()) startCommand();
                return null;
            }
        }.execute();
    }

    @Override
    public void onCompletion(SpeedTestReport report) {
        System.out.println(name + " completed: bit/s: " + report.getTransferRateBit());
        updateHistory(report.getTransferRateBit().doubleValue());
        startTask();
    }

    @Override
    public void onProgress(float percent, SpeedTestReport report) {
        System.out.println(name + " progress: " + percent + "%, bit/s: " + report.getTransferRateBit());
        updateHistory(report.getTransferRateBit().doubleValue());
    }

    @Override
    public void onError(SpeedTestError speedTestError, String errorMessage) {
        Log.e(TAG, name + " error: " + errorMessage + " while "
                + (!task.isCancelled() ? "running" : "terminating"));
        if (!task.isCancelled()) {
            console.putMessage("ERR: " + name + " link test: " + errorMessage);
        }
    }

    private void startCommand() {
        switch (type) {
            case DOWN:
                socket.startDownload("http://2.testdebit.info/fichiers/5Mo.dat", UPDATE_INTERVAL);
                break;
            case UP:
                socket.startUpload("http://2.testdebit.info/", 5000000, UPDATE_INTERVAL);
                break;
        }
    }

    private void initializeHistory() {
        history = new double[HISTORY_LENGTH];
        for (int i = 0; i < HISTORY_LENGTH; ++i) {
            history[i] = -1.;
        }
        historyPosition = 0;
    }

    private void updateHistory(double rate) {
        lock.lock();
        history[historyPosition] = rate;
        historyPosition++;
        if (historyPosition >= HISTORY_LENGTH) historyPosition = 0;
        lock.unlock();
    }

    private double computeDownLink() {
        double result = 0;
        int n = 0;
        lock.lock();
        for (double v : history) {
            if (v != -1.) {
                result += v;
                ++n;
            }
        }
        lock.unlock();
        if (n > 0) return result / n;
        else return 0;
    }
}
