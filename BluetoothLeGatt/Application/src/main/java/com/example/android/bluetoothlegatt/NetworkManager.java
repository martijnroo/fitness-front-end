package com.example.android.bluetoothlegatt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by romanfilippov on 24.04.15.
 */

public class NetworkManager {

    private List _listeners = new ArrayList();
    private double[] _heartBeats;
    private String[] _timestamps;

    RestAdapter restAdapter;
    MeasurementsAPI msrApi;

    private static NetworkManager ourInstance = new NetworkManager();
    public static NetworkManager getInstance() {
        return ourInstance;
    }
    private NetworkManager() {

        restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://52.16.112.21/")
                .build();

        msrApi = restAdapter.create(MeasurementsAPI.class);
    }

    public void sendMeasurementsData(List<Measurement> measurements) {

        msrApi.pushMeasurements(measurements, new Callback<List<Measurement>>() {
            @Override
            public void success(List<Measurement> measurements, Response response) {
                System.out.println("SENT! response:"+response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("FAIL TO SEND! Error:"+retrofitError);
            }
        });
    }

    public void getMeasurements() {
        msrApi.measurements(new Callback<List<Measurement>>() {
            @Override
            public void success(List<Measurement> measurements, Response response) {
                System.out.println("GET MEASUREMENTS:"+response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("FAIL TO GET MEASUREMENTS:"+retrofitError);
            }
        });
    }

    public synchronized void addMoodListener( NetworkManagerListener l ) {
        _listeners.add( l );
    }

    public synchronized void removeMoodListener( NetworkManagerListener l ) {
        _listeners.remove( l );
    }

    private synchronized void _fireMeasureEvent () {
        MeasurementData mood = new MeasurementData( this, _heartBeats, _timestamps );
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (NetworkManagerListener) listeners.next() ).measurementDataReceived(mood);
        }
    }

}
