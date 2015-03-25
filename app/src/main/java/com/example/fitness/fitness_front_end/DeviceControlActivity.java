package com.example.fitness.fitness_front_end;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shangxor on 3/25/15.
 * activity to display data
 */
public class DeviceControlActivity extends Activity {
    private final String TAG = DeviceControlActivity.class.getSimpleName();
    public static final String testDevName = "test name";
    public static final String testDevAddress = "test address";

    private TextView conState, dataField;
    private String devName, devAddress;
    private ExpandableListView serviceList;
    private BLE ble;
    private boolean conStatus = false;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> gattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private BluetoothGattCharacteristic notifyCharacteristic;

    private final String testName = "Name";
    private final String testUUID = "UUID";

    // service connection
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName cName, IBinder service) {
            ble = ((BLE.LocalBinder) service).getService();
            if (!ble.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            //auto connect if initialized
            ble.connect(devAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ble = null;
        }
    };

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        devName = intent.getStringExtra(testDevName);
        devAddress = intent.getStringExtra(testDevAddress);

        ((TextView) findViewById(R.id.device_address)).setText(devAddress);

    }

    /**
     * gatt services: connect, disconnect, discover, retrieve data
     */

    private final BroadcastReceiver gattUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

        }
    };
}
