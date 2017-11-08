package com.nawbar.networkmeasurements.measurements.models;

import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class CellData {

    private static final String TAG = CellData.class.getSimpleName();

    public enum CellType {
        GSM,
        WCDMA,
        LTE
    }

    private CellType type;
    private int mnc;
    private int mcc;
    private int cellId;
    private int areaCode;
    private int dbm;

    public CellData(CellType type, int mnc, int mcc, int cellId, int areaCode, int dbm) {
        this.type = type;
        this.mnc = mnc;
        this.mcc = mcc;
        this.cellId = cellId;
        this.areaCode = areaCode;
        this.dbm = dbm;
    }

    public CellData(CellInfoGsm cellInfo) {
        this.type = CellType.GSM;
        this.mnc = cellInfo.getCellIdentity().getMnc();
        this.mcc = cellInfo.getCellIdentity().getMcc();
        this.cellId = cellInfo.getCellIdentity().getCid();
        this.areaCode = cellInfo.getCellIdentity().getLac();
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoWcdma cellInfo) {
        this.type = CellType.WCDMA;
        this.mnc = cellInfo.getCellIdentity().getMnc();
        this.mcc = cellInfo.getCellIdentity().getMcc();
        this.cellId = cellInfo.getCellIdentity().getCid();
        this.areaCode = cellInfo.getCellIdentity().getLac();
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoLte cellInfo) {
        this.type = CellType.LTE;
        this.mnc = cellInfo.getCellIdentity().getMnc();
        this.mcc = cellInfo.getCellIdentity().getMcc();
        this.cellId = cellInfo.getCellIdentity().getCi();
        this.areaCode = cellInfo.getCellIdentity().getTac();
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellType getType() {
        return type;
    }

    public int getMnc() {
        return mnc;
    }

    public int getMcc() {
        return mcc;
    }

    public int getCellId() {
        return cellId;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public int getDbm() {
        return dbm;
    }

    @Override
    public String toString() {
        return "CellData{" +
                "type=" + type +
                ", mnc=" + mnc +
                ", mcc=" + mcc +
                ", cellId=" + cellId +
                ", areaCode=" + areaCode +
                ", dbm=" + dbm +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CellData cellData = (CellData) o;

        if (mnc != cellData.mnc) return false;
        if (mcc != cellData.mcc) return false;
        if (cellId != cellData.cellId) return false;
        if (areaCode != cellData.areaCode) return false;
        return type == cellData.type;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + mnc;
        result = 31 * result + mcc;
        result = 31 * result + cellId;
        result = 31 * result + areaCode;
        return result;
    }

    JSONObject toJson() throws JSONException {
        // TODO fix this, signal strength HAS to be in defined range
        int ss = Math.max(-113, Math.min(-51, dbm));
        if (ss != dbm) {
            Log.e(TAG, " ===== Signal strength clamped to acceptable value !!! =====");
        }

        JSONObject result = new JSONObject();
        result.put("signal_strength", ss);
        JSONObject cell = new JSONObject();
        cell.put("cell_type", type.toString());
        cell.put("mnc", mnc);
        cell.put("mcc", mcc);
        cell.put("cell_id", cellId);
        cell.put("area_code", areaCode);
        result.put("cell", cell);
        return result;
    }
}
