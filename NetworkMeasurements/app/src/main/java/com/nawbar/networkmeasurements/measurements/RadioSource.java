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
            Log.e(TAG, "Found cells: " + cells.size());
            boolean foundRegistered = false;
            for (CellInfo info : cells) {
                if (info.isRegistered()) {
                    pushCell(measurement, info);
                    foundRegistered = true;
                    Log.e(TAG, "Found registered cell: " + info.toString());
                    break;
                }
            }
            if (foundRegistered) {
                for (CellInfo info : cells) {
                    if (!info.isRegistered()) {
                        pushCell(measurement, info);
                    }
                }
            }
        }
//        int size = (int)(Math.random() * 3) + 1;
//        for (int i = 0; i < size; i++) {
//            int s = (int)(-60 - Math.random() * 30);
//            int cellId = 2147483647;
//            int areaCode = 2147483647;
//            if (i == 0) {
//                cellId = 1700494;
//                areaCode = 206;
//            }
//            CellData cd = new CellData(CellData.CellType.WCDMA, 260, 6, cellId, areaCode, s);
//            measurement.addCell(cd);
//        }
        Log.e(TAG, measurement.toString());
        return measurement;
    }

    private void pushCell(Radio measurement, CellInfo info) {
        if (info instanceof CellInfoGsm) {
            if (isValidGsmCell((CellInfoGsm) info)) {
                measurement.addCell(new CellData((CellInfoGsm) info));
            } else Log.e(TAG, "Invalid GSM cell: " + info.toString());
        } else if (info instanceof CellInfoWcdma) {
            if (isValidWcdmaCell((CellInfoWcdma) info)) {
                measurement.addCell(new CellData((CellInfoWcdma) info));
            } else Log.e(TAG, "Invalid WCDMA cell: " + info.toString());
        } else if (info instanceof CellInfoLte) {
            if (isValidLteCell((CellInfoLte) info)) {
                measurement.addCell(new CellData((CellInfoLte) info));
            } else Log.e(TAG, "Invalid LTE cell: " + info.toString());
        } else {
            Log.i(TAG, "Unknown cell type");
        }
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
