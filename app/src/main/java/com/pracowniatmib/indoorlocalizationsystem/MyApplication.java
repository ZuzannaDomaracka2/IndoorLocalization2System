package com.pracowniatmib.indoorlocalizationsystem;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;

import java.util.ArrayList;
import java.util.List;

final public class MyApplication extends Application {

private ArrayList<AlgorithmOption> algorithmOptionList;
private BluetoothAdapter bluetoothAdapter  = null;
private List<Integer> majorList;
private List<Integer> minorList;
private List<Integer> rssiList;

public ArrayList<AlgorithmOption> getAlgorithmOptionList() { return algorithmOptionList; }
public BluetoothAdapter getBluetoothAdapter() { return bluetoothAdapter; }
public List<Integer> getMajorList() { return majorList; }
public List<Integer> getMinorList() { return minorList; }
public List<Integer> getRssiList() { return rssiList; }

public void setAlgorithmOptionList(ArrayList<AlgorithmOption> algorithmOptionList) { this.algorithmOptionList = algorithmOptionList; }
public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) { this.bluetoothAdapter = bluetoothAdapter; }
public void setMajorList(List<Integer> majorList) { this.majorList = majorList; }
public void setMinorList(List<Integer> minorList) { this.minorList = minorList; }
public void setRssiList(List<Integer> rssiList) { this.rssiList = rssiList; }

}
