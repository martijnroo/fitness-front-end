package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MainActivity extends Activity {

    private List<String> your_array_list;
    BluetoothAdapter btAdapter;
    BluetoothManager btManager;
    private final static String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothGatt bGatt;
    private String address = "00:22:D0:61:03:86";

    private Map<String, ArrayList<Integer>> measurements = new HashMap<String, ArrayList<Integer>>();

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);



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
        query.put("user_id","0");

        MainListener listener = new MainListener();
        NetworkManager.getInstance().addNetworkListener(listener);
        NetworkManager.getInstance().getExercises(query);

        your_array_list = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
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
        bGatt = device.connectGatt(this, false, gattCallback);
        Log.d(TAG, String.format("Name: %s", device.getName()));
        //bGatt.discoverServices();
        return true;
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        bGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }


        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            // this will get called anytime you perform a read or write characteristic operation

            Log.w(TAG, "Characteristic change.");
            int flag = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            int format = -1;
            Integer rr_interval;
            rr_interval = 0;
            long time;
            String time_stamp = null;
            DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");


            if ((flag & 0x10) != 0){
                format = BluetoothGattCharacteristic.FORMAT_UINT16;

                for(int i=2; i<=10; i+=2){
                    rr_interval = characteristic.getIntValue(format,i);
                    if (rr_interval != null) {

                        time = System.currentTimeMillis();
                        time_stamp = formatter.format(new Date(time));
                        // Log.d(TAG, String.format("time: %s", time_stamp));


                        /**if (rr_interval != null) {
                         intent.putExtra(EXTRA_DATA, rr_interval.toString());
                         }*/

                        if (!measurements.containsKey(time_stamp)) {
                            measurements.put(time_stamp, new ArrayList<Integer>());
                            if (rr_interval != null) measurements.get(time_stamp).add(rr_interval);
                        } else {
                            if (rr_interval != null) measurements.get(time_stamp).add(rr_interval);
                        }
                    }
                   // sendBroadcast(intent);
                }

            }

            JSONObject json = new JSONObject(measurements);
            try {
                Log.d(TAG, String.format("Measurements: %s", json.toString(1)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
            // called when use discoverServices

            List<BluetoothGattService> services = bGatt.getServices();

            //A GATT operation completed successfully
            //Constant Value: 0 (0x00000000)
            Log.w(TAG, "onServicesDiscovered received: " + status);

            for (BluetoothGattService service : services) {
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                    for (BluetoothGattCharacteristic characteristic : characteristics){
                        Log.w(TAG,"characteristic: "+characteristic.getUuid());
                        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
                            bGatt.setCharacteristicNotification(characteristic, true);
                            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            bGatt.writeDescriptor(descriptor);
                        }
                    }
            }

        }
    };



}
