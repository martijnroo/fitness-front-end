package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class GraphActivity extends Activity implements NetworkManagerListener {

    private LineGraphSeries<DataPoint> mSeries;
    private RequestQueue mRequestQueue;
    String url = "http://52.16.112.21/measurements/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // ADD NETWORK MANAGER LISTENER TO RECEIVE CALLBACKS

        NetworkManager.getInstance().addNetworkListener(this);

//        //----------------------------------------------------
//        // EXAMPLE OF SENDING DATA
//        // CREATE MEASUREMENT, >=1
//        Measurement m1 = new Measurement();
//        m1.user_id = 9;
//        m1.rr_value = 585;
//
//        // ADD MEASUREMENTS TO ARRAYLIST
//        ArrayList<Measurement> msr = new ArrayList<>();
//        msr.add(m1);
//
//        // USE NETWORK MANAGER API TO SEND
//        NetworkManager.getInstance().sendMeasurementsData(msr);
//
//        //----------------------------------------------------
//        // EXAMPLE OF GETTING DATA
//        // CREATE HASHMAP WITH QUERY ARGS
//        HashMap<String, String> query = new HashMap<>();
//        query.put("user_id","9");
//
//        // USE NETWORK MANAGET TO GET THE DATA
//        // THEN USE CALLBACK BELOW
//
//        NetworkManager.getInstance().getMeasurements(query);
//
//        Exercise e1 = new Exercise();
//        e1.type = "walking";
//        e1.user_id = 9;
//        e1.start = new Date();
//        e1.end = new Date();
//
//        ArrayList<Exercise> exr = new ArrayList<>();
//        exr.add(e1);
//
//        NetworkManager.getInstance().sendExercisesData(exr);

        HashMap<String, String> exr_query = new HashMap<>();
        exr_query.put("user_id","4");

        NetworkManager.getInstance().getExercises(exr_query);
    }

    public void measurementDataReceived(MeasurementData data){
        // CALLBACK FROM NETWORK MANAGER WHEN DATA IS RECEIVED
        // TO ACCESS the data use data.measurements() property
        System.out.println("RECEIVED CALLBACK FIRE, data=" + data.measurements());
    }
    public void measurementDataSent(){
        // CALLBACK FROM NETWORK MANAGER WHEN DATA IS SENT
        System.out.println("SEND CALLBACK FIRE");
    }

    public void exerciseDataReceived(ExerciseData data){
        System.out.println("RECEIVED EXERCISES:"+data.exercises());
    }

    public void exerciseDataSent(){
        System.out.println("EXERCISES SENT!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



//    public void makeRequest() {
//        mRequestQueue =  Volley.newRequestQueue(this);
//        JsonObjectRequest jsObjRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//
//                            System.out.println("Response: " + response.toString());
//                            JSONArray msr = response.getJSONArray("measurements");
//
//                            if (msr.length() >0 ) {
//
//                                double[] hr = new double[msr.length()];
//                                Date[] dates = new Date[msr.length()];
//                                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//
//                                for (int i = 0; i < msr.length(); i++) {
//
//                                    hr[i] = (double) (msr.getJSONObject(i).getInt("heart_rate"));
//                                    String curDate = (String) (msr.getJSONObject(i).getString("timestamp"));
//                                    dates[i] = formatter.parse(curDate);
//
//                                }
//
//                                HashMap<String, Object> graphData = new HashMap<>();
//                                graphData.put("measurements",hr);
//                                graphData.put("timestamps", dates);
//
//                                GraphFragment gf = (GraphFragment)
//                                        getFragmentManager().findFragmentById(R.id.graph_fragment);
//
//                                gf.setGraphData(graphData);
//                                System.out.println("Received hbeats: "+hr);
//                                System.out.println("Received timestamps: "+dates);
//
////                                GraphView graph = (GraphView) findViewById(R.id.graph);
////
////                                double min = (double)(msr.getJSONObject(0).getInt("heart_rate"));
////                                double max = min;
////
////                                DataPoint[] dp = new DataPoint[msr.length()];
////                                for (int i = 0; i < msr.length(); i++) {
////                                    double val = (double)(msr.getJSONObject(i).getInt("heart_rate"));
////
////                                    if (val < min)
////                                        min = val;
////
////                                    if (val > max)
////                                        max = val;
////
////                                    dp[i] = new DataPoint(i, val);
////                                    System.out.println("Point:" +val);
////                                }
////
////                                mSeries.resetData(dp);
////                                System.out.println("New Data Loaded on the Graph!");
////
////                                graph.getViewport().setYAxisBoundsManual(true);
////                                graph.getViewport().setXAxisBoundsManual(true);
////
////                                if (min == max) {
////                                    min -= 2;
////                                    max += 2;
////                                }
////                                graph.getViewport().setMinY(min);
////                                graph.getViewport().setMaxY(max);
////
////                                graph.getViewport().setMinX(0);
////                                graph.getViewport().setMaxX(msr.length()-1);
//                            }
//
//                        } catch (Exception e) {
//                            System.out.println(e.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        System.out.println(error);
//
//                    }
//                });
//
//        mRequestQueue.add(jsObjRequest);
//    }
