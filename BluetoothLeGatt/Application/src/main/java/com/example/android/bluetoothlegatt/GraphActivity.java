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


public class GraphActivity extends Activity {

    private LineGraphSeries<DataPoint> mSeries;
    private RequestQueue mRequestQueue;
    String url = "http://52.16.112.21/measurements/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Test "Initial" Data

        GraphView graph = (GraphView) findViewById(R.id.graph);
        mSeries = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 2),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 1),
        });
        graph.addSeries(mSeries);

        makeRequest();
    }

    public void makeRequest() {
        mRequestQueue =  Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            System.out.println("Response: " + response.toString());
                            JSONArray msr = response.getJSONArray("measurements");

                            if (msr.length() >0 ) {

                                GraphView graph = (GraphView) findViewById(R.id.graph);

                                double min = (double)(msr.getJSONObject(0).getInt("heart_rate"));
                                double max = min;

                                DataPoint[] dp = new DataPoint[msr.length()];
                                for (int i = 0; i < msr.length(); i++) {
                                    double val = (double)(msr.getJSONObject(i).getInt("heart_rate"));

                                    if (val < min)
                                        min = val;

                                    if (val > max)
                                        max = val;

                                    dp[i] = new DataPoint(i, val);
                                    System.out.println("Point:" +val);
                                }

                                mSeries.resetData(dp);
                                System.out.println("New Data Loaded on the Graph!");

                                graph.getViewport().setYAxisBoundsManual(true);
                                graph.getViewport().setXAxisBoundsManual(true);

                                if (min == max) {
                                    min -= 2;
                                    max += 2;
                                }
                                graph.getViewport().setMinY(min);
                                graph.getViewport().setMaxY(max);

                                graph.getViewport().setMinX(0);
                                graph.getViewport().setMaxX(msr.length()-1);
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println(error);

                    }
                });

        mRequestQueue.add(jsObjRequest);
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
