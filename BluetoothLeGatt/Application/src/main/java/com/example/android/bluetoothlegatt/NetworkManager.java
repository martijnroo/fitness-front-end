package com.example.android.bluetoothlegatt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.database.Observable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

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

        Gson gson = new Gson();
        String msrm = gson.toJson(measurements);

        HashMap<String,String> finalJson = new HashMap<>();
        finalJson.put("measurements", msrm);

        String finalJsonString = gson.toJson(finalJson);

        System.out.println("GENERATED JSON:"+finalJsonString);

        msrApi.pushMeasurements(finalJsonString, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                System.out.println("Send!" + response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("FAIL:" + retrofitError);
            }
        });
    }

    private Measurement[] convertJSONToMeasurements(JsonElement jsonElement) {
        JsonArray ar = jsonElement.getAsJsonObject().getAsJsonArray("measurements");
        Measurement[] result = new Measurement[ar.size()];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        try {
            for (int i = 0; i < result.length; i++) {

                JsonObject msr = ar.get(i).getAsJsonObject();

                result[i] = new Measurement();
                result[i].id = msr.get("id").getAsInt();
                result[i].rr_value = msr.get("rr_value").getAsInt();
                result[i].timestamp = formatter.parse(msr.get("timestamp").getAsString());
                result[i].user_id = msr.get("user_id").getAsInt();
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /*
     *  Returns measurements from the server
     *  See https://martijnroo.github.io/fitness-back-end/ for possible parameters
     */
    public void getMeasurements(HashMap<String,String> params) {

        msrApi.measurements(params, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                Measurement[] msr = convertJSONToMeasurements(jsonElement);

                for (int i = 0; i < msr.length; i++) {
                    System.out.println("Response: "+ msr[i].timestamp);
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("FAIL" + retrofitError);
            }
        });
    }

//    public void getMeasurements() {
//        msrApi.measurements(new Callback<List<Measurement>>() {
//            @Override
//            public void success(List<Measurement> measurements, Response response) {
//                System.out.println("GET MEASUREMENTS:"+response);
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                System.out.println("FAIL TO GET MEASUREMENTS:"+retrofitError);
//            }
//        });
//    }

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
