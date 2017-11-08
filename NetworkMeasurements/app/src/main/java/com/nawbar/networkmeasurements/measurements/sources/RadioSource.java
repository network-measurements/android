package com.nawbar.networkmeasurements.measurements.sources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;
import com.nawbar.networkmeasurements.measurements.models.CellData;
import com.nawbar.networkmeasurements.measurements.models.Radio;

import java.util.List;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class RadioSource {

    private static final String TAG = RadioSource.class.getSimpleName();

    private final TelephonyManager telephonyManager;
    private final ConsoleInput console;

    public RadioSource(Context context, ConsoleInput console) {
        this.telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        this.console = console;
    }

    public Radio measure() {
        Radio measurement = new Radio();
        @SuppressLint("MissingPermission") // application checks it on startup
        List<CellInfo> cells = telephonyManager.getAllCellInfo();
        if (cells != null) {
            Log.e(TAG, "Found cells: " + cells.size());
            boolean foundRegistered = false;
            for (CellInfo info : cells) {
                if (info.isRegistered()) {
                    CellData data = getValidCellData(info);
                    if (data != null) {
                        measurement.setRegistered(data);
                    }
                    foundRegistered = true;
                    Log.e(TAG, "Found registered cell: " + info.toString());
                    break;
                }
            }
            if (foundRegistered) {
                for (CellInfo info : cells) {
                    CellData data = getValidCellData(info);
                    if (!info.isRegistered() && data != null) {
                        measurement.addCell(data);
                    }
                }
            }
        }
        Log.e(TAG, measurement.toString());
        return measurement;
    }

    private CellData getValidCellData(CellInfo info) {
        if (info instanceof CellInfoGsm) {
            if (isValidGsmCell((CellInfoGsm) info)) {
                return new CellData((CellInfoGsm) info);
            } else Log.e(TAG, "Invalid GSM cell: " + info.toString());
        } else if (info instanceof CellInfoWcdma) {
            if (isValidWcdmaCell((CellInfoWcdma) info)) {
                return new CellData((CellInfoWcdma) info);
            } else Log.e(TAG, "Invalid WCDMA cell: " + info.toString());
        } else if (info instanceof CellInfoLte) {
            if (isValidLteCell((CellInfoLte) info)) {
                return new CellData((CellInfoLte) info);
            } else Log.e(TAG, "Invalid LTE cell: " + info.toString());
        } else {
            Log.i(TAG, "Unknown cell type");
            return null;
        }
        return null;
    }

    private boolean isValidGsmCell(CellInfoGsm info) {
        return true;
    }

    private boolean isValidWcdmaCell(CellInfoWcdma info) {
        return true;
    }

    private boolean isValidLteCell(CellInfoLte info) {
        return true;
    }
}
