package com.nawbar.networkmeasurements.server_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nawbar.networkmeasurements.server_data.CellData.CellType.GSM;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Radio {

    private static final String TAG = Radio.class.getSimpleName();

    private List<CellData> cells;

    public Radio() {
        this.cells = new ArrayList<>();
    }

    public void addCell(CellData cellData) {
        cells.add(cellData);
    }

    public List<CellData> getCells() {
        return cells;
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
        for (CellData data : cells) {
            array.put(data.toJson());
        }
        result.put("cell_measurements", array);
        return result;
    }

    public String getShortString() {
        if (cells != null && cells.size() > 0) {
            int gsm = 0, wcdma = 0, lte = 0;
            int reg_strength = cells.get(0).getDbm();
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
