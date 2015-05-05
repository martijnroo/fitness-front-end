package com.example.android.bluetoothlegatt;

import java.util.Date;

/**
 * Created by romanfilippov on 24.04.15.
 */
public class Measurement implements Comparable<Measurement> {

    public int rr_value;
    public String id;
    public Date timestamp;
    public int user_id;

    public int compareTo( Measurement o ) {
        return timestamp.compareTo(o.timestamp);
    }
}
