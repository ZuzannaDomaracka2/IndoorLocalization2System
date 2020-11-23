package com.pracowniatmib.indoorlocalizationsystem;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    int REQUEST_ENABLE_BT = 1;
    String userId;

    Button buttonStart;
    Button buttonCheckDbConnection;
    Button buttonCheckPermissions;
    Button buttonEnableBt;
    WifiManager wifiManager;

    DatabaseReference dataBuildings;
    DatabaseReference dataUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        buttonStart = findViewById(R.id.buttonStartMenu);
        buttonCheckDbConnection = findViewById(R.id.buttonCheckDbConnMenu);
        buttonCheckPermissions = findViewById(R.id.buttonCheckPermissionsMenu);
        buttonEnableBt = findViewById(R.id.buttonEnableBtMenu);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        dataBuildings = FirebaseDatabase.getInstance().getReference("Buildings");
        dataUsers = FirebaseDatabase.getInstance().getReference("Users");
        addBuildings();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateListener, filter);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });
        buttonCheckDbConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CHECK DB CONNECTION BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            }
        });
        buttonCheckPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "CHECK PERMISSIONS BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonEnableBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(MainActivity.this, "DEVICE DOES NOT SUPPORT BLUETOOTH!", Toast.LENGTH_SHORT).show();
                } else if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    Toast.makeText(MainActivity.this, "BLUETOOTH IS ENABLED!", Toast.LENGTH_SHORT).show();
                    buttonEnableBt.setText("BLUETOOTH IS ENABLED");
                }
            }
        });

        userId = dataUsers.push().getKey();
        dataUsers.child(userId).child("userId").setValue(userId);
        dataUsers.child(userId).child("building_floor").setValue("1");
        dataUsers.child(userId).child("building_id").setValue("123");
        dataUsers.child(userId).child("coordinates").child("x").setValue("0");
        dataUsers.child(userId).child("coordinates").child("y").setValue("0");
    }

    public void addBuildings(){
        //Building1
        String id1=dataBuildings.push().getKey();
        dataBuildings.child(id1).child("map_name").setValue("map_1");
        dataBuildings.child(id1).child("name").setValue("building1");

        // BLE Transmitter 1
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("power").setValue("1");


        //BLE Trasmitter 2
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("power").setValue("1");

        //WifiTrasmitter1
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("power").setValue("1");

        //Wifitransmitter2
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("power").setValue("1");

        //Building 2
        String id2=dataBuildings.push().getKey();

        dataBuildings.child(id2).child("map_name").setValue("map_2");
        dataBuildings.child(id2).child("name").setValue("building2");

        // BLE Transmitter 1
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("power").setValue("1");

        //BLE Trasmitter 2
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("power").setValue("1");

        //WifiTrasmitter1
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("power").setValue("1");

        //Wifitransmitter2
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("power").setValue("1");
    }

    final BroadcastReceiver bluetoothStateListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        buttonEnableBt.setText(R.string.enable_bluetooth);
                        Toast.makeText(MainActivity.this, "BLUETOOTH IS DISABLED!", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        buttonEnableBt.setText(R.string.disabling_bluetooth);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        buttonEnableBt.setText(R.string.bluetooth_is_enabled);
                        Toast.makeText(MainActivity.this, "BLUETOOTH IS ENABLED!", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        buttonEnableBt.setText(R.string.enabling_bluetooth);
                        break;
                }
            }
        }
    };

    /*@Override
    protected void onDestroy() {
        //delete user database record
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userReference.removeValue();

        //unregister Bluetooth state listener
        unregisterReceiver(bluetoothStateListener);

        super.onDestroy();
    }*/

    @Override
    protected void onStop() {
        //delete user database record
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userReference.removeValue();

        //unregister Bluetooth state listener
        unregisterReceiver(bluetoothStateListener);
        super.onStop();
    }
}

