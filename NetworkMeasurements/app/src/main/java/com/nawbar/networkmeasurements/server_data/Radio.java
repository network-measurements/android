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

    private int registeredCellCgi;
    private CellData.CellType registeredCellType;
    private String registeredOperator;

    private List<CellData> cells;

    public Radio() {
        this.cells = new ArrayList<>();
    }

    public void addCell(CellData cellData, boolean registered) {
        cells.add(cellData);
        if (registered) {
            registeredCellCgi = cells.get(cells.size() - 1).getCgi();
            registeredCellType = cells.get(cells.size() - 1).getType();
        }
    }

    public List<CellData> getCells() {
        return cells;
    }

    public int getRegisteredCgi() {
        return registeredCellCgi;
    }

    public void setRegisteredCgi(int registeredCgi) {
        this.registeredCellCgi = registeredCgi;
    }

    public String getRegisteredOperator() {
        return registeredOperator;
    }

    public void setRegisteredOperator(String registeredOperator) {
        this.registeredOperator = registeredOperator;
    }

    @Override
    public String toString() {
        return "Radio{" +
                "registeredCgi=" + registeredCellCgi +
                ", registeredOperator=" + registeredOperator +
                ", cells=" + cells +
                '}';
    }

    public JSONObject toJson(long time) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("time", time);
        result.put("registered_cell_id", registeredCellCgi);
        result.put("registered_cell_type", registeredCellType);
        result.put("registered_operator", registeredOperator);
        JSONArray array = new JSONArray();
        for (CellData data : cells) {
            array.put(data.toJson());
        }
        result.put("cell_measurements", array);
        return result;
    }

    public String getShortString() {
        if (cells != null && cells.size() > 0) {
            int gsm = 0, cdma = 0, wcdma = 0, lte = 0;
            for (CellData c : cells) {
                switch (c.getType()) {
                    case GSM: ++gsm; break;
                    case CDMA: ++cdma; break;
                    case WCDMA: ++wcdma; break;
                    case LTE: ++lte; break;
                }
            }
            return "GSM[" + gsm + "], CDMA[" + cdma + "] WCDMA[" + wcdma + "] LTE[" + lte + "]";
        } else {
            return "Empty network";
        }
    }
}
