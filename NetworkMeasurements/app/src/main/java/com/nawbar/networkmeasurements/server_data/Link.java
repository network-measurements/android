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

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("up_link", upLink);
        result.put("down_link", downLink);
        result.put("latency", latency);
        return result;
    }

    public double getUpLinkInMbPS() {
        return upLink / 1000000;
    }

    public double getDownLinkInMbPS() {
        return downLink / 1000000;
    }
}
