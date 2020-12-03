package com.pracowniatmib.indoorlocalizationsystem;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_ENABLE_BT = 1;
    final int REQUEST_LOCATION = 2;
    final int REQUEST_BLUETOOTH = 3;
    final int REQUEST_WIFI = 4;
    MyApplication myApplication;

    String userId;

    Context activityContext = this;

    Button buttonStart;
    Button buttonCheckDbConnection;
    Button buttonCheckPermissions;
    Button buttonEnableBt;

    DatabaseReference dataBuildings;
    DatabaseReference dataUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        myApplication = (MyApplication) getApplication();

        //get handles to UI objects
        buttonStart = findViewById(R.id.buttonStartMenu);
        buttonCheckDbConnection = findViewById(R.id.buttonCheckDbConnMenu);
        buttonCheckPermissions = findViewById(R.id.buttonCheckPermissionsMenu);
        buttonEnableBt = findViewById(R.id.buttonEnableBtMenu);

        //register Bluetooth state update receiver
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateListener, filter);

        //set OnClickListeners to buttons
        buttonStart.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MapActivity.class)));

        buttonCheckDbConnection.setOnClickListener(view -> Toast.makeText(MainActivity.this, "CHECK DB CONNECTION BUTTON CLICKED!", Toast.LENGTH_SHORT).show());

        buttonCheckPermissions.setOnClickListener(view -> checkPermissions(activityContext));

        buttonEnableBt.setOnClickListener(view -> {
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
        });

        //read/write from/to database
        dataBuildings = FirebaseDatabase.getInstance().getReference("Buildings");
        dataUsers = FirebaseDatabase.getInstance().getReference("Users");
        addBuildings();
        userId = dataUsers.push().getKey();
        dataUsers.child(userId).child("userId").setValue(userId);
        dataUsers.child(userId).child("building_floor").setValue("1");
        dataUsers.child(userId).child("building_id").setValue("123");
        dataUsers.child(userId).child("coordinates").child("x").setValue("0");
        dataUsers.child(userId).child("coordinates").child("y").setValue("0");
    }

    public void addBuildings(){
        //Building 1
        String id1=dataBuildings.push().getKey();
        dataBuildings.child(id1).child("map_name").setValue("map_1");
        dataBuildings.child(id1).child("name").setValue("building1");

        //BLE Transmitter 1
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("power").setValue("1");


        //BLE Transmitter 2
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("power").setValue("1");

        //Wifi Transmitter 1
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("power").setValue("1");

        //Wifi Transmitter 2
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id1).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("power").setValue("1");

        //Building 2
        String id2=dataBuildings.push().getKey();

        dataBuildings.child(id2).child("map_name").setValue("map_2");
        dataBuildings.child(id2).child("name").setValue("building2");

        //BLE Transmitter 1
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 1").child("power").setValue("1");

        //BLE Transmitter 2
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("BLE Transmitters").child("BLE Transmitter 2").child("power").setValue("1");

        //Wifi Transmitter 1
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 1").child("power").setValue("1");

        //Wifi transmitter 2
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("x").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("coordinates").child("y").setValue("1");
        dataBuildings.child(id2).child("floors").child("0").child("WiFi Transmitters").child("WiFi Transmitter 2").child("power").setValue("1");
    }

    public void updateUserDatabase(String buildingId, String buildingFloor, float x, float y) {
        Map<String, Object> coordinateHashMap = new HashMap<>();
        coordinateHashMap.put("x", String.valueOf(x));
        coordinateHashMap.put("y", String.valueOf(y));

        Map<String, Object> buildingHashMap = new HashMap<>();
        buildingHashMap.put("building_floor", buildingFloor);
        buildingHashMap.put("building_id", buildingId);

        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("coordinates").updateChildren(coordinateHashMap);
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).updateChildren(buildingHashMap);
    }

    public void checkPermissions(Context context) {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_BLUETOOTH);
        }
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE}, REQUEST_WIFI);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Location permissions were granted", Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity.this, "Location permissions were not granted", Toast.LENGTH_LONG).show();
                break;
            case REQUEST_BLUETOOTH:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Bluetooth permissions were granted", Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity.this, "Bluetooth permissions were not granted", Toast.LENGTH_LONG).show();
                break;
            case REQUEST_WIFI:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "WiFi permissions were granted", Toast.LENGTH_LONG).show();
                } else Toast.makeText(MainActivity.this, "WiFi permissions were not granted", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
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

    @Override
    protected void onStop() {
        //delete user database record
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userReference.removeValue();

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //unregister Bluetooth state listener
        unregisterReceiver(bluetoothStateListener);

        super.onDestroy();
    }
}

