package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;


public class ExerciseDetailActivity extends Activity implements NetworkManagerListener {

    private GraphFragment graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_exercise_detail);

        int position = getIntent().getExtras().getInt("pos");

        // TODO: Roman, fetch the exercise details from the server and update the following fields
        TextView title = (TextView) findViewById(R.id.detail_exercise_title);
        TextView time = (TextView) findViewById(R.id.detail_exercise_time);
        TextView name = (TextView) findViewById(R.id.detail_exercise_name);
        TextView activity = (TextView) findViewById(R.id.detail_exercise_activity);
        TextView notes = (TextView) findViewById(R.id.detail_exercise_notes);

        title.setText("Exercise " + position);
        // time.setText(...);

        // Create a new Graph Fragment to be placed in the top part of Exercise
        graph = new GraphFragment();

        // TODO: Roman, fetch the measurement data by clicked listposition (in MainActivity) and update the graph data in fragment here!
        //graph.setArguments(getIntent().getExtras());

        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(position);
        String strI = sb.toString();

        HashMap<String, String> query = new HashMap<>();
        query.put("exercise_id", strI);
        query.put("user_id", "7");

        // USE NETWORK MANAGET TO GET THE DATA
        // THEN USE CALLBACK BELOW

        NetworkManager.getInstance().addNetworkListener(this);
        NetworkManager.getInstance().getMeasurements(query);

        getFragmentManager().beginTransaction()
                .add(R.id.view_detail_top, graph).commit();

    }

    public void measurementDataReceived(MeasurementData data){
        Measurement[] msr = data.measurements();

        System.out.println("MSR RECEIVED. START CONFIGURING GRAPH");

        graph.setGraphData(msr);
    }

    public void measurementDataSent(){

    }


    public void exerciseDataReceived(ExerciseData data){

    }

    public void exerciseDataSent(){

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_detail, menu);
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
