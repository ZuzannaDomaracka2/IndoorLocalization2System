package com.pracowniatmib.indoorlocalizationsystem;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

public class SensorChecker {
    public static boolean checkSensorsForDeadReckoning(Context context) {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        boolean gyroscope = manager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null;
        boolean accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null;
        boolean magnetometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null;
        if(gyroscope && accelerometer && magnetometer) {
            Toast.makeText(context, "Sensors for Dead Reckoning are supported", Toast.LENGTH_LONG).show();
            return true;
        }
        else {
            Toast.makeText(context, "Can't use Dead Reckoning algorithm", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
