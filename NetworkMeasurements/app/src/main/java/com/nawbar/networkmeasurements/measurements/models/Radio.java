package com.nawbar.networkmeasurements.measurements.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Radio {

    private static final String TAG = Radio.class.getSimpleName();

    private CellData registered;
    private Set<CellData> cells;

    public Radio() {
        this.registered = null;
        this.cells = new HashSet<>();
    }

    public void setRegistered(CellData cellData) {
        registered = cellData;
    }

    public void addCell(CellData cellData) {
        if (!cellData.equals(registered)) {
            if (!cells.add(cellData)) {
                Log.e(TAG, "Already containing cell, skipping");
            }
        } else {
            Log.e(TAG, "Skipping registered cell");
        }
    }

    @Override
    public String toString() {
        return "Radio{" +
                "registered=" + registered +
                ", cells=" + cells +
                '}';
    }

    public JSONObject toJson(long time) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("time", time);
        JSONArray array = new JSONArray();
        array.put(registered.toJson());
        for (CellData data : cells) {
            array.put(data.toJson());
        }
        result.put("cell_measurements", array);
        return result;
    }

    public String getShortString() {
        if (registered != null) {
            int gsm = 0, wcdma = 0, lte = 0;
            switch (registered.getType()) {
                case GSM: ++gsm; break;
                case WCDMA: ++wcdma; break;
                case LTE: ++lte; break;
            }
            int reg_strength = registered.getDbm();
            if (cells != null) {
                for (CellData c : cells) {
                    switch (c.getType()) {
                        case GSM: ++gsm; break;
                        case WCDMA: ++wcdma; break;
                        case LTE: ++lte; break;
                    }
                }
            }
            return "GSM[" + gsm + "], WCDMA[" + wcdma + "] LTE[" + lte + "], RSS: "
                    + reg_strength + "dBm";
        } else {
            return "Empty network";
        }
    }
}
