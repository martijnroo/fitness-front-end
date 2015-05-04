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
                .setLogLevel(RestAdapter.LogLevel.FULL)
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

    public void sendExercisesData(List<Exercise> exercises) {

        HashMap<String,List<Exercise>> finalJson = new HashMap<>();
        finalJson.put("exercises", exercises);

        msrApi.pushExercises(finalJson, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                System.out.println("Exercises Send! response:" + response.getBody());

                _fireExerciseSentEvent();
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

                JsonElement el = null;

                el = msr.get("id");
                if (el != null)
                    result[i].id = el.getAsString();

                el = msr.get("rr_value");
                if (el != null)
                    result[i].rr_value = el.getAsInt();

                el = msr.get("timestamp");
                if (el != null)
                    result[i].timestamp = formatter.parse(el.getAsString());

                el = msr.get("user_id");
                if (el != null)
                    result[i].user_id = el.getAsInt();
            }

            return result;
        } catch (Exception e) {
            System.out.println("JSON TO MEASUREMENT CONVERSION ERROR:"+e.getMessage());
            return null;
        }
    }

    private Exercise[] convertJSONToExercises(JsonElement jsonElement) {
        JsonArray ar = jsonElement.getAsJsonObject().getAsJsonArray("exercises");
        Exercise[] result = new Exercise[ar.size()];

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        try {
            for (int i = 0; i < result.length; i++) {

                JsonObject msr = ar.get(i).getAsJsonObject();

                result[i] = new Exercise();
                JsonElement el = null;

                el = msr.get("id");
                if (el != null)
                    result[i].id = el.getAsString();

                el = msr.get("type");
                if (el != null)
                    result[i].type = el.getAsString();

                el = msr.get("avg_rr_value");
                if (el != null)
                    result[i].avg_rr_value = el.getAsString();

                el = msr.get("start");
                if (el != null)
                    result[i].start = formatter.parse(el.getAsString());

                el = msr.get("end");
                if (el != null)
                    result[i].end = formatter.parse(el.getAsString());

                el = msr.get("user_id");
                if (el != null)
                    result[i].user_id = el.getAsInt();
            }

            return result;
        } catch (Exception e) {
            System.out.println("JSON TO EXERCISE CONVERSION ERROR:"+e.getMessage());
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

    /*
     *  Returns measurements from the server
     *  See https://martijnroo.github.io/fitness-back-end/ for possible parameters
     */
    public void getExercises(HashMap<String,String> params) {

        msrApi.exercises(params, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                Exercise[] msr = convertJSONToExercises(jsonElement);

                _fireExerciseEvent(msr);

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

    private synchronized void _fireExerciseEvent (Exercise[] exercises) {
        ExerciseData mood = new ExerciseData( this, exercises );
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (NetworkManagerListener) listeners.next() ).exerciseDataReceived(mood);
        }
    }

    private synchronized void _fireMeasureSentEvent () {
        Iterator listeners = _listeners.iterator();
        while (listeners.hasNext()) {
            ((NetworkManagerListener) listeners.next()).measurementDataSent();
        }
    }

    private synchronized void _fireExerciseSentEvent () {
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (NetworkManagerListener) listeners.next() ).exerciseDataSent();
        }
    }

}
