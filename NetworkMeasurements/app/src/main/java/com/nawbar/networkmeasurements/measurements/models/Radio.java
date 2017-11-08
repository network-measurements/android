package com.nawbar.networkmeasurements.measurements.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        this.cells = new HashSet<>();
    }

    public void setRegistered(CellData cellData) {
        registered = cellData;
    }

    public void addCell(CellData cellData) {
        if (cellData != registered) {
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
                "cells=" + cells +
                '}';
    }

    public JSONObject toJson(long time) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("time", time);
        JSONArray array = new JSONArray();
        array.put(registered);
        for (CellData data : cells) {
            array.put(data.toJson());
        }
        result.put("cell_measurements", array);
        return result;
    }

    public String getShortString() {
        if (cells != null && cells.size() > 0) {
            int gsm = 0, wcdma = 0, lte = 0;
            int reg_strength = registered.getDbm();
            for (CellData c : cells) {
                switch (c.getType()) {
                    case GSM: ++gsm; break;
                    case WCDMA: ++wcdma; break;
                    case LTE: ++lte; break;
                }
            }
            return "GSM[" + gsm + "], WCDMA[" + wcdma + "] LTE[" + lte + "], RSS: "
                    + reg_strength + "dBm";
        } else {
            return "Empty network";
        }
    }
}
