package com.example.android.bluetoothlegatt;

import java.util.EventObject;

/**
 * Created by romanfilippov on 24.04.15.
 */
public class MeasurementData extends EventObject {

    private double[] _heartBeats;
    private String[] _timestamps;

    public MeasurementData( Object source, double[] heartBeats, String[] timestamps ) {
        super( source );
        _heartBeats = heartBeats;
        _timestamps = timestamps;
    }

    public MeasurementData( Object source ) {
        super( source );
    }

    public double[] heartBeats() {
        return _heartBeats;
    }

    public String[] timestamps() {
        return _timestamps;
    }
}
