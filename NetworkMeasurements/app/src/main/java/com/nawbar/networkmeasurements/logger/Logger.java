package com.nawbar.networkmeasurements.logger;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Bartosz Nawrot on 2017-11-01.
 */

public class Logger implements LoggerInput {
    private static final String TAG = Logger.class.getSimpleName();

    private Context context;
    private ConsoleInput console;

    private BufferedWriter writer;

    public Logger(Context context, ConsoleInput console) {
        this.context = context;
        this.console = console;
        this.writer = null;
    }

    @Override
    public void initialize(String name) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "Initializing logs file, name: \"" + name + "\"");
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), name + ".txt");
        try {
            writer = new BufferedWriter(new FileWriter(file, true));
            console.putMessage("SYS: Log file created: \"" + name + "\"");
        } catch (IOException e) {
            e.printStackTrace();
            writer = null;
            console.putMessage("SYS: Could not open logs file: \"" + name + "\"");
        }
    }

    @Override
    public void log(String message) {
        if (writer != null) {
            try {
                writer.append(message);
                writer.append("\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
