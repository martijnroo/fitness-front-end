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
import android.widget.EditText;


public class ExerciseFormActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_exercise_form);

        final Button saveButton = (Button) findViewById(R.id.button_save_exercise);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.v("button", "SAVE");
                // Click listener for Save button

                String name = ((EditText) findViewById(R.id.edit_text_exercise)).getText().toString();
                String activity = ((EditText) findViewById(R.id.edit_activity_exercise)).getText().toString();
                String notes = ((EditText) findViewById(R.id.edit_notes_exercise)).getText().toString();

                // Timer start and end times in the format required by the server
                String startTime = getIntent().getExtras().getString("timer_start");
                String endTime = getIntent().getExtras().getString("timer_end");

                Log.v("ExerciseFormActivity.java - start", startTime);
                Log.v("ExerciseFormActivity.java - end", endTime);
                Log.v("ExerciseFormActivity.java - name", name);

                // TODO: Alex, Roman, Send the exercise data to the server /exercises/
                // send the name, activity, notes, startTime and endTime + a list of exercises

                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    // don't add it on top of the stack!
                startActivity(intent);
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.button_cancel_exercise);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Click listener for Cancel button
                Log.v("button", "CANCEL");

                onBackPressed();
                // or finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_form, menu);
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
