package com.nawbar.networkmeasurements.measurements;

import android.content.Context;

import com.nawbar.networkmeasurements.server_data.Link;
import com.nawbar.networkmeasurements.view.ConsoleInput;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class LinkSource {

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
}
