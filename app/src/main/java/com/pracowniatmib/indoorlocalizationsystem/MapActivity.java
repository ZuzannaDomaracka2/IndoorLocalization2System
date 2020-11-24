package com.pracowniatmib.indoorlocalizationsystem;

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
    private List<User> user;

    FragmentManager fragmentManager;
    MapFragment mapFragment;

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
        user = new LinkedList<>();

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
                System.out.println("X: " + mapFragment.getMapX() + ", Y: " + mapFragment.getMapY());
                mapFragment.moveMap(0,10);
                mapFragment.rotateCursor(30);
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
                mapFragment.moveMap(0, 10);
                mapFragment.setCursorRotation(0);
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.moveMap(-10, 0);
                mapFragment.setCursorRotation(90);
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.moveMap(10, 0);
                mapFragment.setCursorRotation(270);
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment.moveMap(0, -10);
                mapFragment.setCursorRotation(180);
            }
        });
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.mapViewFragment);
        //mapFragment.setMap(R.raw.polanka_0p_10cm);
        //mapFragment.moveMap(400, -400);
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
            mapFragment.getView().setScaleX(scaleFactor);
            mapFragment.getView().setScaleY(scaleFactor);
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