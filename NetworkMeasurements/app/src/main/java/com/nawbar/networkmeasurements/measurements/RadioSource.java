package com.nawbar.networkmeasurements.measurements;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nawbar.networkmeasurements.view.ConsoleInput;
import com.nawbar.networkmeasurements.server_data.CellData;
import com.nawbar.networkmeasurements.server_data.Radio;

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
        List<CellInfo> cells = telephonyManager.getAllCellInfo();
        if (cells != null) {
            for (CellInfo info : cells) {
                Log.e(TAG, "Cell: " + info);
                if (info instanceof CellInfoGsm) {
                    if (isValidGsmCell((CellInfoGsm) info)) {
                        measurement.addCell(new CellData((CellInfoGsm) info), info.isRegistered());
                    }
                } else if (info instanceof CellInfoCdma) {
                    if (isValidCdmaCell((CellInfoCdma) info)) {
                        measurement.addCell(new CellData((CellInfoCdma) info), info.isRegistered());
                    }
                } else if (info instanceof CellInfoWcdma) {
                    if (isValidWcdmaCell((CellInfoWcdma) info)) {
                        measurement.addCell(new CellData((CellInfoWcdma) info), info.isRegistered());
                    }
                } else if (info instanceof CellInfoLte) {
                    if (isValidLteCell((CellInfoLte) info)) {
                        measurement.addCell(new CellData((CellInfoLte) info), info.isRegistered());
                    }
                } else {
                    Log.i(TAG, "Unknown cell type");
                }
            }
        }
        measurement.setRegisteredOperator(telephonyManager.getNetworkOperator());
        Log.e(TAG, measurement.toString());
        return measurement;
    }

    private boolean isValidGsmCell(CellInfoGsm info) {
        return true;
    }

    private boolean isValidWcdmaCell(CellInfoWcdma info) {
        return true;
    }

    private boolean isValidCdmaCell(CellInfoCdma info) {
        return true;
    }

    private boolean isValidLteCell(CellInfoLte info) {
        return true;
    }
}
