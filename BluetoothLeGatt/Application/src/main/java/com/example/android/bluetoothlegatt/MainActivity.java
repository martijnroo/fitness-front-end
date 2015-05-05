package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private List<String> your_array_list;
    BluetoothAdapter btAdapter;
    BluetoothManager btManager;
    private ArrayAdapter<String> arrayAdapter;
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothGatt bGatt;
    private String address = "00:22:D0:61:03:86";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.view_main_top) != null) {
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            RecordFragment recFragment = new RecordFragment();
            MainFragment mainFragment = new MainFragment();

            //recFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.view_main_top, recFragment).commit();
        }

        ListView lv = (ListView) findViewById(R.id.exercise_list);

        HashMap<String, String> query = new HashMap<>();
        query.put("user_id","7");

        your_array_list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list );


        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.v("LIST", position + " Click'd");

                // The data can be stored in a bundle, if required.
                // Maybe fetch it directly from the server?
                Bundle data = new Bundle();
                data.putInt("pos", position);

                Intent intent = new Intent(getApplication(), ExerciseDetailActivity.class);
                intent.putExtras(data);

                startActivity(intent);

            }
        });

        MainListener listener = new MainListener();
        NetworkManager.getInstance().addNetworkListener(listener);
        NetworkManager.getInstance().getExercises(query);

        if (!initialize())
            Log.e(TAG, "Unable to initialize Bluetooth");

        if (!btAdapter.isEnabled()) {
            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        connect();

    }

    public class MainListener implements NetworkManagerListener {
        @Override
        public void measurementDataReceived(MeasurementData data){

            Log.v("CALLBACK:", "DATA RECEIVED!");
            for (Measurement m : data.measurements()) {
                //Log.v("FROM THE SERVER: ", String.valueOf(m.rr_value));
            }
        }

        @Override
        public void measurementDataSent() {
            Log.d("CALLBACK:", "DATA SENT!");
        }

        public void exerciseDataReceived(ExerciseData data){

            int i=0;
            for (Exercise ex : data.exercises()) {

                your_array_list.add("Exercise "+i);
                i++;

            }

            if (i > 0)
                arrayAdapter.notifyDataSetChanged();

        }
        public void exerciseDataSent(){ Log.d("CALLBACK:", "DATA SENT!"); }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public static class MainFragment extends Fragment {

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }


    public boolean initialize() {
        if (btManager == null) {
            btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (btManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        btAdapter = btManager.getAdapter();
        if (btAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public boolean connect(){
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        bGatt = device.connectGatt(this, true, gattCallback);
        Log.d(TAG, String.format("Name: %s", device.getName()));
        return true;
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation
        }

        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            // this will get called when a device connects or disconnects
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // this will get called after the client initiates a            BluetoothGatt.discoverServices() call
        }
    };



}
