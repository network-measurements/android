package com.nawbar.networkmeasurements.measurements;

import android.os.AsyncTask;
import android.util.Log;

import com.nawbar.networkmeasurements.server_data.Link;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 * https://github.com/bertrandmartel/speed-test-lib
 */

public class LinkSource {

    private static final String TAG = LinkSource.class.getSimpleName();

    private static final int START_DELAY = 1000;
    private static final int UPDATE_INTERVAL = 1000;
    private static final int HISTORY_LENGTH = 4;

    private ConsoleInput consoleInput;

    private SpeedTestSocket downLinkSocket;
    private SpeedTestSocket upLinkSpeed;

    private double downLinkHistory[];
    private double upLinkHistory[];
    private int downLinkHistoryPosition;
    private int upLinkHistoryPosition;
    private Lock downLock = new ReentrantLock();
    private Lock upLock = new ReentrantLock();

    private Link actualLink;

    public LinkSource(ConsoleInput console) {
        this.consoleInput = console;
        this.actualLink = new Link();
        this.downLinkSocket = new SpeedTestSocket();
        this.upLinkSpeed = new SpeedTestSocket();
        this.downLinkSocket.addSpeedTestListener(getDownLinkListener());
        this.upLinkSpeed.addSpeedTestListener(getUpLinkListener());
    }

    public void start() {
        initializeHistory();
        startDownLinkTask();
        startUpLinkTask();
    }

    public void terminate() {
        if (downLinkSocket != null) downLinkSocket.closeSocket();
        if (upLinkSpeed != null) upLinkSpeed.closeSocket();
    }

    private void startDownLinkTask() {
        AsyncTask<Void, Void, String> taskDown = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.e(TAG, "Starting down link measurements with delay: " + START_DELAY + "ms");
                try {
                    Thread.sleep(START_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                downLinkSocket.startDownload("http://2.testdebit.info/fichiers/5Mo.dat", UPDATE_INTERVAL);
                return null;
            }
        };
        taskDown.execute();
    }

    private void startUpLinkTask() {
        AsyncTask<Void, Void, String> taskUp = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.e(TAG, "Starting up link measurements with delay: " + START_DELAY + "ms");
                try {
                    Thread.sleep(START_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                upLinkSpeed.startUpload("http://2.testdebit.info/", 5000000, UPDATE_INTERVAL);
                return null;
            }
        };
        taskUp.execute();
    }

    private ISpeedTestListener getDownLinkListener() {
        return new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                System.out.println("[DOWN COMPLETED] rate in bit/s: " + report.getTransferRateBit());
                //updateDownLinkHistory(report.getTransferRateBit().doubleValue());
                startDownLinkTask();
            }
            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                System.out.println("[DOWN] progress: " + percent + "%, bit/s: " + report.getTransferRateBit());
                //updateDownLinkHistory(report.getTransferRateBit().doubleValue());
            }
            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                Log.e(TAG, "DOWN " + errorMessage);
                consoleInput.putMessage("ERR: DownLink test: " + errorMessage);
            }
        };
    }

    private ISpeedTestListener getUpLinkListener() {
        return new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                System.out.println("[UP COMPLETED] rate in bit/s: " + report.getTransferRateBit());
                //updateUpLinkHistory(report.getTransferRateBit().doubleValue());
                startUpLinkTask();
            }
            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                System.out.println("[UP] progress: " + percent + "%, bit/s: " + report.getTransferRateBit());
                //updateUpLinkHistory(report.getTransferRateBit().doubleValue());
            }
            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {
                Log.e(TAG, "UP " + errorMessage);
                consoleInput.putMessage("ERR: UpLink test: " + errorMessage);
            }
        };
    }

    private void initializeHistory() {
        downLinkHistory = new double[]{-1., -1., -1.};
        upLinkHistory = new double[]{-1., -1., -1.};
        downLinkHistoryPosition = 0;
        upLinkHistoryPosition = 0;
    }

    private void updateDownLinkHistory(double rate) {
        downLock.lock();
        downLinkHistory[downLinkHistoryPosition] = rate;
        downLinkHistoryPosition++;
        if (downLinkHistoryPosition >= HISTORY_LENGTH) downLinkHistoryPosition = 0;
        downLock.unlock();
    }

    private double computeDownLink() {
        double result = 0;
        int n = 0;
        downLock.lock();
        for (double v : downLinkHistory) {
            if (v != -1.) {
                result += v;
                ++n;
            }
        }
        downLock.unlock();
        return result / n;
    }

    private void updateUpLinkHistory(double rate) {
        upLock.lock();
        upLinkHistory[upLinkHistoryPosition] = rate;
        upLinkHistoryPosition++;
        if (upLinkHistoryPosition >= HISTORY_LENGTH) upLinkHistoryPosition = 0;
        upLock.unlock();
    }

    private double computeUpLink() {
        double result = 0;
        int n = 0;
        upLock.lock();
        for (double v : upLinkHistory) {
            if (v != -1.) {
                result += v;
                ++n;
            }
        }
        upLock.unlock();
        return result / n;
    }

    public Link getActualLink() {
        actualLink.downLink = computeDownLink();
        actualLink.upLink = computeUpLink();
        actualLink.latency = 0;
        return actualLink;
    }
}
