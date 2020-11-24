package com.pracowniatmib.indoorlocalizationsystem;

import android.graphics.BitmapFactory;
import android.icu.number.Scale;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity {

    private List<Transmitter> wifiTranmitter;
    private List<Transmitter> bleTranmitter;

    FragmentManager fragmentManager;
    MapFragment mapViewFragment;

    Button buttonSettings;
    Button buttonUpdateMap;
    Button buttonSensorMode;
    Button buttonAlgorithmMode;
    Button buttonTestMap;
    Button buttonUp;
    Button buttonRight;
    Button buttonLeft;
    Button buttonDown;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Objects.requireNonNull(getSupportActionBar()).hide();
        wifiTranmitter = new LinkedList<>();
        bleTranmitter = new LinkedList<>();

        fragmentManager = getSupportFragmentManager();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        buttonSettings = findViewById(R.id.buttonSettingsMap);
        buttonUpdateMap = findViewById(R.id.buttonUpdateMap);
        buttonSensorMode = findViewById(R.id.buttonSensorModeMap);
        buttonAlgorithmMode = findViewById(R.id.buttonAlgorithmModeMap);
        buttonTestMap = findViewById(R.id.buttonTestMap);
        buttonUp = findViewById(R.id.buttonUp);
        buttonRight = findViewById(R.id.buttonRight);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonDown = findViewById(R.id.buttonDown);
        buttonTestMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("X: " + mapViewFragment.getMapX() + ", Y: " + mapViewFragment.getMapY());
                mapViewFragment.moveMap(0,10);
                mapViewFragment.rotateCursor(30);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "SETTINGS BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpdateMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "UPDATE MAP BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSensorMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "SENSOR MODE BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAlgorithmMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MapActivity.this, "ALGORITHM MODE BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapViewFragment.moveMap(0, 10);
                mapViewFragment.setCursorRotation(0);
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapViewFragment.moveMap(-10, 0);
                mapViewFragment.setCursorRotation(90);
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapViewFragment.moveMap(10, 0);
                mapViewFragment.setCursorRotation(270);
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapViewFragment.moveMap(0, -10);
                mapViewFragment.setCursorRotation(180);
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mapViewFragment = (MapFragment) fragmentManager.findFragmentById(R.id.mapViewFragment);
        //mapViewFragment.setMap(R.drawable.default_indoor_map);
        mapViewFragment.setMap(R.raw.polanka_0p_10cm);
        mapViewFragment.moveMap(400, -400);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector)
        {
            scaleFactor *= scaleGestureDetector.getScaleFactor();
            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 8.0f));
            mapViewFragment.getView().setScaleX(scaleFactor);
            mapViewFragment.getView().setScaleY(scaleFactor);
            return true;
        }
    }

    public void addNewWiFiTransmitter(String ID,  double xCoord, double yCoord, double transmitPower){
        wifiTranmitter.add(new Transmitter(ID, xCoord, yCoord, transmitPower));
    }
    public void addNewBleTransmitter(String ID,  double xCoord, double yCoord, double transmitPower){
        bleTranmitter.add(new Transmitter(ID, xCoord, yCoord, transmitPower));
    }
}