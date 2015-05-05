package com.example.android.bluetoothlegatt;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by romanfilippov on 24.04.15.
 */
public interface MeasurementsAPI {

    @GET("/measurements/")
    public void measurements(@QueryMap Map<String, String> options, Callback<JsonElement> cb);

    @POST("/measurements/")
    public void pushMeasurements(@Body HashMap<String,List<Measurement>> measurements, Callback<Response> cb);

    @GET("/exercises/")
    public void exercises(@QueryMap Map<String, String> options, Callback<JsonElement> cb);

    @POST("/exercises/")
    public void pushExercise(@Body Exercise exercise, Callback<Response> cb);
}
