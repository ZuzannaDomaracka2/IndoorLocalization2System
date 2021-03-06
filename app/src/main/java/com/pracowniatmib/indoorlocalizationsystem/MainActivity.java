package com.pracowniatmib.indoorlocalizationsystem;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_ENABLE_BT = 1;
    final int REQUEST_LOCATION = 2;
    final int REQUEST_BLUETOOTH = 3;
    final int REQUEST_WIFI = 4;

    MyApplication myApplication;
    Context activityContext = this;

    Button buttonStart;
    Button buttonCheckSensors;
    Button buttonCheckPermissions;
    Button buttonEnableBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        myApplication = (MyApplication) getApplication();

        //get handles to UI objects
        buttonStart = findViewById(R.id.buttonStartMenu);
        buttonCheckSensors = findViewById(R.id.buttonCheckSensors);
        buttonCheckPermissions = findViewById(R.id.buttonCheckPermissionsMenu);
        buttonEnableBt = findViewById(R.id.buttonEnableBtMenu);

        //register Bluetooth state update receiver
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateListener, filter);

        //setup the algorithm options
        ArrayList<AlgorithmOption> algorithmOptionList = myApplication.getAlgorithmOptionList();
        if(algorithmOptionList == null)
        {
            algorithmOptionList = new ArrayList<>();
            algorithmOptionList.add(new AlgorithmOption(getString(R.string.dead_reckoning), R.drawable.dead_reckoning_icon));
            algorithmOptionList.add(new AlgorithmOption(getString(R.string.trilateration), R.drawable.trilateration_icon));
            algorithmOptionList.add(new AlgorithmOption(getString(R.string.fingerprinting), R.drawable.fingerprinting_icon));
        }
        myApplication.setAlgorithmOptionList(algorithmOptionList);

        //set OnClickListeners to buttons
        buttonStart.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MapActivity.class)));

        buttonCheckSensors.setOnClickListener(view -> myApplication.setDeadReckoningAvailable(checkSensorsArePresent(activityContext)) );

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

    public boolean checkSensorsArePresent(Context context) {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        boolean gyroscope = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null;
        boolean accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null;
        boolean magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null;
        if(gyroscope && accelerometer && magnetometer) {
            Toast.makeText(context, "Sensors required for Dead Reckoning are supported", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(context, "Can't use Dead Reckoning algorithm", Toast.LENGTH_SHORT).show();
            return false;
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
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //unregister Bluetooth state listener
        unregisterReceiver(bluetoothStateListener);
        super.onDestroy();
    }
}

