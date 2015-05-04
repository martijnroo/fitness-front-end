package com.example.android.bluetoothlegatt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by romanfilippov on 24.04.15.
 */

public class NetworkManager {

    private List _listeners = new ArrayList();

    RestAdapter restAdapter;
    MeasurementsAPI msrApi;

    private static NetworkManager ourInstance = new NetworkManager();
    public static NetworkManager getInstance() {
        return ourInstance;
    }
    private NetworkManager() {

        restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint("http://52.16.112.21/")
                .build();

        msrApi = restAdapter.create(MeasurementsAPI.class);
    }

    public void sendMeasurementsData(List<Measurement> measurements) {

        HashMap<String,List<Measurement>> finalJson = new HashMap<>();
        finalJson.put("measurements", measurements);

        msrApi.pushMeasurements(finalJson, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                System.out.println("Measurements Send! response:" + response.getBody());

                _fireMeasureSentEvent();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("Measurements Send FAIL:" + retrofitError);
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
                //result[i].id = msr.get("id").getAsInt();
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

                _fireMeasureEvent(msr);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("FAIL" + retrofitError);
            }
        });
    }

    public synchronized void addNetworkListener( NetworkManagerListener l ) {
        _listeners.add( l );
    }

    public synchronized void removeNetworkListener( NetworkManagerListener l ) {
        _listeners.remove( l );
    }

    private synchronized void _fireMeasureEvent (Measurement[] measurements) {
        MeasurementData mood = new MeasurementData( this, measurements );
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (NetworkManagerListener) listeners.next() ).measurementDataReceived(mood);
        }
    }

    private synchronized void _fireMeasureSentEvent () {
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (NetworkManagerListener) listeners.next() ).measurementDataSent();
        }
    }

}
