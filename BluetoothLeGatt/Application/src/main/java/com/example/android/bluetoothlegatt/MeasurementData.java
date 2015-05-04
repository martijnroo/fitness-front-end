package com.example.android.bluetoothlegatt;

import java.util.EventObject;

/**
 * Created by romanfilippov on 24.04.15.
 */
public class MeasurementData extends EventObject {

    private Measurement[] _measurements;

    public MeasurementData( Object source, Measurement[] measurements ) {
        super( source );
        _measurements = measurements;
    }

    public MeasurementData( Object source ) {
        super( source );
    }

    public Measurement[] measurements() {
        return _measurements;
    }

}
