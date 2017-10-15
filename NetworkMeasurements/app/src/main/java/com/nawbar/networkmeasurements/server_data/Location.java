package com.nawbar.networkmeasurements.server_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Location {
    public double latitude;
    public double longitude;

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("lat", latitude);
        result.put("lng", longitude);
        return result;
    }
}
