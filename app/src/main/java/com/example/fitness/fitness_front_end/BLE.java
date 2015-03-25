package com.example.fitness.fitness_front_end;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by shangxor on 3/25/15.
 * Service for connecting and managing data
 * between phone and belt.
 */
public class BLE extends Service {
    private final static String TAG = BLE.class.getSimpleName();
    private BluetoothManager bManager;
    private BluetoothAdapter bAdapter;
    private String bDevAddress;
    private BluetoothGatt bGatt;
    private final IBinder binder = new LocalBinder();
    private int conState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;

    public class LocalBinder extends Binder {
        BLE getService() {
            return BLE.this;
        }
    }

    @Override
    public IBinder onBind (Intent intent) {
        return binder;
    }

    /**
     * disconnect function
     */
    public void disconnect() {
        if (bAdapter == null || bGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bGatt.disconnect();
    }

    /**
     * close() function for releasing resources
     */
    public void close() {
        if (bGatt == null) {
            return;
        }
        bGatt.close();
        bGatt = null;
    }
}
