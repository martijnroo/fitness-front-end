package com.example.android.bluetoothlegatt;

/**
 * Created by romanfilippov on 24.04.15.
 */
public interface NetworkManagerListener {

    public void measurementDataReceived(MeasurementData data);
    public void measurementDataSent();
}
