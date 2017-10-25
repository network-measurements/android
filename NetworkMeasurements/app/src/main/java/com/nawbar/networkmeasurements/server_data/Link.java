package com.nawbar.networkmeasurements.server_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Link {
    public double upLink = 0; // [b/s]
    public double downLink = 0; // [b/s]
    public double latency = 0; // [ms]

    public JSONObject toJson(long time) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("time", time);
        result.put("uplink", (long)upLink);
        result.put("downlink", (long)downLink);
        result.put("latency", (long)latency);
        return result;
    }

    public double getUpLinkInMbps() {
        return upLink / 1000000;
    }

    public double getDownLinkInMbps() {
        return downLink / 1000000;
    }
}
