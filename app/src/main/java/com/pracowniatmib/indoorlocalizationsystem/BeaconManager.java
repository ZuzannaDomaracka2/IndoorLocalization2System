package com.pracowniatmib.indoorlocalizationsystem;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BeaconManager {

    private BluetoothAdapter adapter;
    private BluetoothLeScanner scanner;
    private ScanFilter filter;
    private ScanSettings settings;
    private ScanCallback callback;
    private final Context context;
    private String uuidStr = "";
    private int manufacturerId;

    private List<Integer> majorList = new ArrayList<>();
    private List<Integer> minorList = new ArrayList<>();
    private List<Integer> rssiList = new ArrayList<>();

    public BeaconManager(Context context) {
        this.context = context;
    }

    public BeaconManager(Context context, int manufacturerId, String uuidStr) {
        this.context = context;
        this.manufacturerId = manufacturerId;
        this.uuidStr = uuidStr;
    }

    private void initialize() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        scanner = adapter.getBluetoothLeScanner();
        filter = setScanFilter(manufacturerId, uuidStr);
        settings = setScanSettings();
        callback = setScanCallback(manufacturerId);
    }

    public void runScanning() {
        if(context != null) {
            initialize();
            if(adapter != null) {
                if(adapter.isEnabled()){
                    scanner.startScan(Arrays.asList(filter), settings, callback);
                }
                else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    context.startActivity(intent);
                }

                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        final String action = intent.getAction();
                        if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                            switch(state) {
                                case BluetoothAdapter.STATE_ON:
                                    Toast.makeText(context, "BLUETOOTH IS ENABLED", Toast.LENGTH_LONG).show();
                                    break;
                                case BluetoothAdapter.STATE_TURNING_ON:
                                    scanner.startScan(Arrays.asList(filter), settings, callback);
                                case BluetoothAdapter.STATE_TURNING_OFF:
                                    Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    context.startActivity(bluetoothIntent);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                };

                IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                context.registerReceiver(receiver, intentFilter);
            }
            else {
                Toast.makeText(context, "THIS DEVICE DOESN'T SUPPORT BLUETOOTH", Toast.LENGTH_LONG).show();
            }
        }
        else {
            throw new NullPointerException("CAN'T RECOGNIZE APP CONTEXT");
        }
    }

    private ScanFilter setScanFilter(int manufacturerId, String uuidStr) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        ByteBuffer manufacturerData = ByteBuffer.allocate(24);
        ByteBuffer manufacturerMask = ByteBuffer.allocate(24);
        byte[] uuid = convertUuidToByte(UUID.fromString(uuidStr));
        manufacturerData.put(0, (byte) 0x02);
        manufacturerData.put(1, (byte) 0x15);
        for(int i = 2; i < 18; i++) {
            manufacturerData.put(i, uuid[i-2]);
        }
        for(int i = 0; i < 18; i++) {
            manufacturerMask.put((byte) 0x01);
        }
        builder.setManufacturerData(manufacturerId, manufacturerData.array(), manufacturerMask.array());
        return builder.build();
    }

    private ScanSettings setScanSettings() {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setReportDelay(0);
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER);
        return builder.build();
    }

    private ScanCallback setScanCallback(int manufacturerId) {
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                final byte[] scanData = result.getScanRecord().getManufacturerSpecificData(manufacturerId);
                int major = (scanData[18] & 0xff) * 0x100 + (scanData[19] & 0xff);
                int minor = (scanData[20] & 0xff) * 0x100 + (scanData[21] & 0xff);
                int rssi = result.getRssi();
                boolean flag = false;
                if(!rssiList.isEmpty()) {
                    for(int i = 0; i < rssiList.size(); i++) {
                        if (majorList.get(i) == major && minorList.get(i) == minor) {
                            rssiList.set(i, rssi);
                            flag = true;
                            break;
                        }
                    }
                }
                if(!flag) {
                    majorList.add(major);
                    minorList.add(minor);
                    rssiList.add(rssi);
                }
            }
        };
    }

    private byte[] convertUuidToByte(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public void setUUID(String uuidStr) {
        this.uuidStr = uuidStr;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getUUID() {
        return this.uuidStr;
    }

    public int getManufacturerId() {
        return this.manufacturerId;
    }

    public List<Integer> getMajorList() {
        return majorList;
    }

    public List<Integer> getMinorList() {
        return minorList;
    }

    public List<Integer> getRssiList() {
        return rssiList;
    }

}
