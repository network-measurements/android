package com.nawbar.networkmeasurements.logger;

import android.content.Context;
import android.util.Log;

/**
 * Created by Bartosz Nawrot on 2017-11-01.
 */

public class Logger implements LoggerInput {
    private static final String TAG = Logger.class.getSimpleName();

    public Logger(Context context) {

    }

    @Override
    public void initialize(String name) {
        Log.e(TAG, "Initializing logs file, name: \"" + name + "\"");

    }

    @Override
    public void log(String message) {

    }
}
