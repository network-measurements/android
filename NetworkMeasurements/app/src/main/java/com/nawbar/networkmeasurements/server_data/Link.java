package com.nawbar.networkmeasurements.server_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Link {
    public double upLink;
    public double downLink;
    public double latency;

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("up_link", upLink);
        result.put("down_link", downLink);
        result.put("latency", latency);
        return result;
    }
}
