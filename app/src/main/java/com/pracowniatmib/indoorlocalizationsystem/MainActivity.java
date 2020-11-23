package com.pracowniatmib.indoorlocalizationsystem;

import android.annotation.SuppressLint;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Button buttonStart;
    Button buttonCheckDbConnection;
    Button buttonCheckPermissions;
    Button buttonEnableBt;
    WifiManager wifiManager;


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
        int REQUEST_ENABLE_BT = 1;


        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

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
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        buttonEnableBt.setText("BLUETOOTH IS DISABLED");
                        Toast.makeText(MainActivity.this, "BLUETOOTH IS Disabled!", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        buttonEnableBt.setText("DISABLING BLUETOOTH...");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        buttonEnableBt.setText("BLUETOOTH IS ENABLED");
                        Toast.makeText(MainActivity.this, "BLUETOOTH IS ENABLED!", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        buttonEnableBt.setText("ENABLING BLUETOOTH...");
                        break;
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

