package com.nawbar.networkmeasurements.server_data;

import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
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
    private String cgi;
    private int dbm;

    public CellData(CellType type, String cgi, int dbm) {
        this.type = type;
        this.cgi = cgi;
        this.dbm = dbm;
    }

    public CellData(CellInfoGsm cellInfo) {
        this.type = CellType.GSM;
        this.cgi = identityToString(cellInfo.getCellIdentity());
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoCdma cellInfo) {
        this.type = CellType.CDMA;
        this.cgi = identityToString(cellInfo.getCellIdentity());
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoWcdma cellInfo) {
        this.type = CellType.WCDMA;
        this.cgi = identityToString(cellInfo.getCellIdentity());
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    public CellData(CellInfoLte cellInfo) {
        this.type = CellType.LTE;
        this.cgi = identityToString(cellInfo.getCellIdentity());
        this.dbm = cellInfo.getCellSignalStrength().getDbm();
    }

    private String identityToString(CellIdentityGsm identity) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mcc=").append(identity.getMcc());
        sb.append(",Mnc=").append(identity.getMnc());
        //sb.append(",Lac=").append(identity.getLac());
        sb.append(",Cid=").append(identity.getCid());
        //sb.append(",mArfcn=").append(identityGsm.getArfcn());
        //sb.append(",mBsic=").append("0x").append(Integer.toHexString(identity.getBsic()));
        return sb.toString();
    }

    private String identityToString(CellIdentityCdma identity) {
        StringBuilder sb = new StringBuilder();
        sb.append("NetworkId="); sb.append(identity.getNetworkId());
        sb.append(",SystemId="); sb.append(identity.getSystemId());
        sb.append(",BasestationId="); sb.append(identity.getBasestationId());
        //sb.append(",Longitude="); sb.append(identity.getLongitude());
        //sb.append(",Latitude="); sb.append(identity.getLatitude());
        return sb.toString();
    }

    private String identityToString(CellIdentityWcdma identity) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mcc=").append(identity.getMcc());
        sb.append(",Mnc=").append(identity.getMnc());
        //sb.append(",Lac=").append(identity.getLac());
        sb.append(",Cid=").append(identity.getCid());
        //sb.append(",Psc=").append(identity.getPsc());
        //sb.append(",Uarfcn=").append(identity.getUarfcn());
        return sb.toString();
    }

    private String identityToString(CellIdentityLte identity) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mcc="); sb.append(identity.getMcc());
        sb.append(",Mnc="); sb.append(identity.getMnc());
        //sb.append(",Ci="); sb.append(identity.getCi());
        sb.append(",Pci="); sb.append(identity.getPci());
        //sb.append(",Tac="); sb.append(identity.getTac());
        //sb.append(",Earfcn="); sb.append(identity.getEarfcn());
        return sb.toString();
    }

    public CellType getType() {
        return type;
    }

    public String getCgi() {
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
