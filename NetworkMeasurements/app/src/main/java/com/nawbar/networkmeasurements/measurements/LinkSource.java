package com.nawbar.networkmeasurements.measurements;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.nawbar.networkmeasurements.server_data.Link;
import com.nawbar.networkmeasurements.view.ConsoleInput;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 * https://github.com/bertrandmartel/speed-test-lib
 */

public class LinkSource implements ISpeedTestListener {

    private static final String TAG = LinkSource.class.getSimpleName();

    private Context context;
    private ConsoleInput consoleInput;

    private Link actualLink;

    public LinkSource(Context context, ConsoleInput console) {
        this.context = context;
        this.consoleInput = console;
        this.actualLink = new Link();
    }

    public Link getActualLink() {
        return actualLink;
    }

    public void start() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.e(TAG, "Starting link measurements");
                SpeedTestSocket speedTestSocket = new SpeedTestSocket();
                speedTestSocket.addSpeedTestListener(LinkSource.this);
                //speedTestSocket.startDownload("http://2.testdebit.info/fichiers/1Mo.dat");
                speedTestSocket.startUpload("http://2.testdebit.info/", 1000000);
                return null;
            }
        };
        task.execute();
    }

    public void terminate() {

    }

    @Override
    public void onCompletion(SpeedTestReport report) {
        // called when download/upload is complete
        System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
        System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
    }

    @Override
    public void onError(SpeedTestError speedTestError, String errorMessage) {
        // called when a download/upload error occur
        System.out.println("[ERROR] " + errorMessage);
    }

    @Override
    public void onProgress(float percent, SpeedTestReport report) {
        // called to notify download/upload progress
        System.out.println("[PROGRESS] progress : " + percent + "%");
        System.out.println("[PROGRESS] download in bit/s   : " + report.getTransferRateBit());
        System.out.println("[PROGRESS] upload in bit/s   : " + report.getTransferRateBit());
    }
}
