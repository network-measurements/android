package com.nawbar.networkmeasurements.logger;

import android.content.Context;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;

/**
 * Created by Bartosz Nawrot on 2017-11-01.
 */

public class Logger implements LoggerInput {
    private static final String TAG = Logger.class.getSimpleName();

    private ConsoleInput console;

    public Logger(Context context, ConsoleInput console) {
        this.console = console;
    }

    @Override
    public void initialize(String name) {
        Log.e(TAG, "Initializing logs file, name: \"" + name + "\"");
        console.putMessage("SYS: Log file created: \"" + name + "\"");
    }

    @Override
    public void log(String message) {

    }
}
