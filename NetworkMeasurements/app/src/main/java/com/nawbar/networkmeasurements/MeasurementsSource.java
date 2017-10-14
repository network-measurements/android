package com.nawbar.networkmeasurements;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.nawbar.networkmeasurements.server.CellData;
import com.nawbar.networkmeasurements.server.Measurement;

import java.util.List;

/**
 * Created by Bartosz Nawrot on 2017-10-14.
 */

public class MeasurementsSource {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final TelephonyManager telephonyManager;
    private final ConsoleInput console;

    public MeasurementsSource(ConsoleInput console, Context context) {
        this.telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        this.console = console;

    }
    public Measurement measure() {
        Measurement measurement = new Measurement();
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
        measurement.setConnectedOperator(telephonyManager.getNetworkOperator());
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
