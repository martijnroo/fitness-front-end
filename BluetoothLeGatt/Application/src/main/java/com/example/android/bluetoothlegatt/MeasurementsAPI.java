package com.example.android.bluetoothlegatt;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by romanfilippov on 24.04.15.
 */
public interface MeasurementsAPI {

    @GET("/measurements/")
    public void measurements(Callback<List<Measurement>> cb);

    @POST("/measurements/")
    public void pushMeasurements(@Body List<Measurement> measurements, Callback<List<Measurement>> cb);
}
