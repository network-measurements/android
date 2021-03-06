package com.nawbar.networkmeasurements.server_data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Location {
    public double latitude = 0;
    public double longitude = 0;

    public Location(android.location.Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public JSONObject toJson(long time) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("time", time);
        result.put("lat", latitude);
        result.put("lng", longitude);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
