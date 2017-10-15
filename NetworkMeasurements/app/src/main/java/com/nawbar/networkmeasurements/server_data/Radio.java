package com.nawbar.networkmeasurements.server_data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Radio {

    private int registeredCgi;
    private String registeredOperator;
    private List<CellData> cells;

    public Radio() {
        this.cells = new ArrayList<>();
    }

    public void addCell(CellData cellData, boolean registered) {
        cells.add(cellData);
        if (registered) {
            registeredCgi = cells.get(cells.size() - 1).getCgi();
        }
    }

    public List<CellData> getCells() {
        return cells;
    }

    public int getRegisteredCgi() {
        return registeredCgi;
    }

    public void setRegisteredCgi(int registeredCgi) {
        this.registeredCgi = registeredCgi;
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
                "registeredCgi=" + registeredCgi +
                ", registeredOperator=" + registeredOperator +
                ", cells=" + cells +
                '}';
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("registered_cell_id", registeredCgi);
        result.put("registered_operator_id", registeredOperator);
        JSONArray array = new JSONArray();
        for (CellData data : cells) {
            array.put(data.toJson());
        }
        result.put("cells", array);
        return result;
    }
}
