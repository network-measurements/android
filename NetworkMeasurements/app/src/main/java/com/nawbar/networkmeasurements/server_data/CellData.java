package com.nawbar.networkmeasurements.server_data;

import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class CellData {
    public enum CellType {
        GSM,
        CDMA,
        WCDMA,
        LTE
    }

    private CellType type;
    private int cgi;
    private int dbm;

    public CellData(CellInfoGsm cellInfo) {
        this.type = CellType.GSM;
        this.cgi = 0; // TODO solve CGI
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoCdma cellInfo) {
        this.type = CellType.CDMA;
        this.cgi = 0; // TODO solve CGI
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoWcdma cellInfo) {
        this.type = CellType.WCDMA;
        this.cgi = 0; // TODO solve CGI
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoLte cellInfo) {
        this.type = CellType.LTE;
        this.cgi = 0; // TODO solve CGI
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellType getType() {
        return type;
    }

    public int getCgi() {
        return cgi;
    }

    public int getDbm() {
        return dbm;
    }

    @Override
    public String toString() {
        return "CellData{" +
                "type=" + type +
                ", cgi=" + cgi +
                ", dbm=" + dbm +
                '}';
    }

    public JSONObject toJson() throws JSONException {
        JSONObject result = new JSONObject();
        result.put("cell_id", cgi);
        result.put("cell_type", type.toString());
        result.put("signal_strength", dbm);
        return result;
    }
}
