package com.nawbar.networkmeasurements.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class Measurement {

    private int connectedCgi;
    private String connectedOperator;
    private List<CellData> cells;

    public Measurement() {
        this.cells = new ArrayList<>();
    }

    public void addCell(CellData cellData, boolean registered) {
        cells.add(cellData);
        if (registered) {
            connectedCgi = cells.get(cells.size() - 1).getCgi();
        }
    }

    public List<CellData> getCells() {
        return cells;
    }

    public int getConnectedCgi() {
        return connectedCgi;
    }

    public void setConnectedCgi(int connectedCgi) {
        this.connectedCgi = connectedCgi;
    }

    public String getConnectedOperator() {
        return connectedOperator;
    }

    public void setConnectedOperator(String connectedOperator) {
        this.connectedOperator = connectedOperator;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "connectedCgi=" + connectedCgi +
                ", connectedOperator=" + connectedOperator +
                ", cells=" + cells +
                '}';
    }
}
