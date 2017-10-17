package com.nawbar.networkmeasurements.server_data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("registered_cell_id", registeredCellCgi);
        result.put("registered_cell_type", registeredCellType);
        result.put("registered_operator_id", registeredOperator);
        JSONArray array = new JSONArray();
        for (CellData data : cells) {
            array.put(data.toJson());
        }
        result.put("cells", array);
        return result;
    }
}
