package com.pracowniatmib.indoorlocalizationsystem;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.ArrayMap;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    final int REQUEST_ENABLE_BT = 1;
    final int REQUEST_LOCATION = 2;
    final int REQUEST_BLUETOOTH = 3;
    final int REQUEST_WIFI = 4;

    String userId;

    Context activityContext = this;

    Button buttonStart;
    Button buttonCheckDbConnection;
    Button buttonCheckPermissions;
    Button buttonEnableBt;

    DatabaseReference dataBuildings;
    DatabaseReference dataUsers;

    //intitialize algorithm menu adapter object
    //final AlgorithmMenuAdapter algorithmMenuAdapter = new AlgorithmMenuAdapter(activityContext);
    ArrayMap<String, Boolean> algorithmOptions = new ArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //get handles to UI objects
        buttonStart = findViewById(R.id.buttonStartMenu);
        buttonCheckDbConnection = findViewById(R.id.buttonCheckDbConnMenu);
        buttonCheckPermissions = findViewById(R.id.buttonCheckPermissionsMenu);
        buttonEnableBt = findViewById(R.id.buttonEnableBtMenu);

        //register Bluetooth state update receiver
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateListener, filter);

        //dummy initiate algorithmOptions Map object - the target is to fetch it from Application-scope variable
        algorithmOptions.put("trilateration", true);
        algorithmOptions.put("fingerprinting", true);
        algorithmOptions.put("deadreckoning", true);

        //TODO: import the algorithmMenuAdapter class and make it work with the Map variable

        //set OnClickListeners to buttons
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
                checkPermissions(activityContext);
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

    /*
    void showAlgorithmMenuAlert()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Choose location algorithm")
                .setAdapter(algorithmMenuAdapter, null)
                .setPositiveButton("OK", (dialog, which) -> {
                    if(algorithmMenuAdapter.isFlagSet()) {
                        StringBuilder temp = new StringBuilder();
                        for(int i = 0; i < algorithmList.size(); i++) {
                            if(algorithmMenuAdapter.getChosenAlgorithms().get(i)) {
                                if(temp.toString().equals("")) temp = new StringBuilder(algorithmList.get(i));
                                else temp.append(", ").append(algorithmList.get(i));
                            }
                        }
                        if(!temp.toString().equals("")) {
                            algorithmName = temp.toString();
                            textView.setText(algorithmName);
                        } else {
                            textView.setText("No algorithm has been chosen");
                            algorithmName = "";
                            for (int i = 0; i < algorithmList.size(); i++) {
                                algorithmMenuAdapter.setAlgorithm(i, false);
                            }

                        }
                        algorithmMenuAdapter.setFlag(false);
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(MenuActivity.this, "Nothing was changed", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    for (int i = 0; i < algorithmList.size(); i++) {
                        algorithmMenuAdapter.setAlgorithm(i, algorithmName.contains(algorithmList.get(i)));
                    }
                    algorithmMenuAdapter.setFlag(false);
                    dialog.dismiss();
                    Toast.makeText(MenuActivity.this, "Nothing was changed", Toast.LENGTH_LONG).show();
                });
    }

     */

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

