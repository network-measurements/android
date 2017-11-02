package com.nawbar.networkmeasurements.measurements.sources;

import com.nawbar.networkmeasurements.measurements.models.Link;
import com.nawbar.networkmeasurements.view.ConsoleInput;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class LinkSource {

    private static final String TAG = LinkSource.class.getSimpleName();

    private ConsoleInput console;

    private LinkTestTask upLinkTest;
    private LinkTestTask downLinkTest;
    private LatencyTestTask latencyTest;

    private Link actualLink;

    public LinkSource(ConsoleInput console) {
        this.console = console;
        this.upLinkTest = new LinkTestTask(console, LinkTestTask.Type.UP);
        this.downLinkTest = new LinkTestTask(console, LinkTestTask.Type.DOWN);
        this.latencyTest = new LatencyTestTask(console);
        this.actualLink = new Link();
    }

    public void start() {
        upLinkTest.start();
        downLinkTest.start();
        latencyTest.start();
    }

    public void terminate() {
        upLinkTest.terminate();
        downLinkTest.terminate();
        latencyTest.terminate();
    }

    public Link getActualLink() {
        actualLink.upLink = upLinkTest.getRate();
        actualLink.downLink = downLinkTest.getRate();
        actualLink.latency = latencyTest.getLatency();
        return actualLink;
    }
}
