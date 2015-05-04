package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class ExerciseDetailActivity extends Activity {

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
        GraphFragment graph = new GraphFragment();

        // TODO: Roman, fetch the measurement data by clicked listposition (in MainActivity) and update the graph data in fragment here!
        //graph.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction()
                .add(R.id.view_detail_top, graph).commit();

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
