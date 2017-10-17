package com.nawbar.networkmeasurements.measurements;

import com.nawbar.networkmeasurements.server_data.Link;
import com.nawbar.networkmeasurements.view.ConsoleInput;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class LinkSource {

    private static final String TAG = LinkSource.class.getSimpleName();

    private ConsoleInput console;

    private LinkTestTask upLinkTest;
    private LinkTestTask downLinkTest;

    private Link actualLink;

    public LinkSource(ConsoleInput console) {
        this.console = console;
        this.upLinkTest = new LinkTestTask(console, LinkTestTask.Type.UP);
        this.downLinkTest = new LinkTestTask(console, LinkTestTask.Type.DOWN);
        this.actualLink = new Link();
    }

    public void start() {
        upLinkTest.start();
        downLinkTest.start();
    }

    public void terminate() {
        upLinkTest.terminate();
        downLinkTest.terminate();
    }

    public Link getActualLink() {
        actualLink.downLink = upLinkTest.getRate();
        actualLink.upLink = downLinkTest.getRate();
        actualLink.latency = 0;
        return actualLink;
    }
}
