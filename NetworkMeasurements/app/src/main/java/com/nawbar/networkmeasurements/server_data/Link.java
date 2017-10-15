package com.nawbar.networkmeasurements.server_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Link {
    public double uplink;
    public double downlink;
    public double latency;

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("uplink", uplink);
        result.put("downlink", downlink);
        result.put("latency", latency);
        return result;
    }
}
