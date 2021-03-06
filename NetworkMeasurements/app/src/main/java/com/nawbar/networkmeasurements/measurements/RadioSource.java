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
            for (CellInfo info : cells) {
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
//        int size = (int)(Math.random() * 10) + 1;
//        for (int i = 0; i < size; i++) {
//            int s = (int)(-60 - Math.random() * 30);
//            String sb = "Mcc=" + i +
//                    ",Mnc=" + 2 * i +
//                    ",Ci=" + 3 * i +
//                    ",Pci=" + 4 * i +
//                    ",Tac=" + 5 * i;
//            CellData cd = new CellData(CellData.CellType.LTE, sb, s);
//            measurement.addCell(cd, i == 0);
//        }
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
