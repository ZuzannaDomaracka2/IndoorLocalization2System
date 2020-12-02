package com.pracowniatmib.indoorlocalizationsystem;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapPositionChangeListener {

    Context activityContext = this;

    private List<Transmitter> wifiTransmitterList;
    private List<Transmitter> bleTransmitterList;

    FragmentManager fragmentManager;
    MapFragment mapFragment;

    TextView mapPositionTextView;
    TextView debugTextView;
    Button buttonSettings;
    Button buttonUpdateMap;
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

        //initialize object lists
        wifiTransmitterList = new ArrayList<>();
        bleTransmitterList = new ArrayList<>();

        //dummy initiate algorithmOptions Map object - the target is to fetch it from Application-scope variable
        ArrayList<AlgorithmOption> algorithmOptionsArrayList = new ArrayList<>();
        algorithmOptionsArrayList.add(new AlgorithmOption(getString(R.string.dead_reckoning), R.drawable.dead_reckoning_icon));
        algorithmOptionsArrayList.add(new AlgorithmOption(getString(R.string.trilateration), R.drawable.trilateration_icon));
        algorithmOptionsArrayList.add(new AlgorithmOption(getString(R.string.fingerprinting), R.drawable.fingerprinting_icon));
        final AlgorithmMenuAdapter algorithmMenuAdapter = new AlgorithmMenuAdapter(activityContext, algorithmOptionsArrayList);

        //initialize utility objects
        fragmentManager = getSupportFragmentManager();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        //get handles to UI objects
        mapPositionTextView = findViewById(R.id.mapPositionTextView);
        debugTextView = findViewById(R.id.textDebugMap);
        buttonSettings = findViewById(R.id.buttonSettingsMap);
        buttonUpdateMap = findViewById(R.id.buttonUpdateMap);
        buttonAlgorithmMode = findViewById(R.id.buttonAlgorithmModeMap);
        buttonTestMap = findViewById(R.id.buttonTestMap);
        buttonUp = findViewById(R.id.buttonUp);
        buttonRight = findViewById(R.id.buttonRight);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonDown = findViewById(R.id.buttonDown);

        //set button OnClickListeners
        buttonTestMap.setOnClickListener(v -> {
            System.out.println("X: " + mapFragment.getMapX() + ", Y: " + mapFragment.getMapY());
            mapFragment.moveMap(0,10);
            mapFragment.rotateCursor(30);
        });

        buttonSettings.setOnClickListener(view -> Toast.makeText(MapActivity.this, "SETTINGS BUTTON CLICKED!", Toast.LENGTH_SHORT).show());

        buttonUpdateMap.setOnClickListener(view -> Toast.makeText(MapActivity.this, "UPDATE MAP BUTTON CLICKED!", Toast.LENGTH_SHORT).show());

        buttonAlgorithmMode.setOnClickListener(view -> showAlgorithmMenuAlert(algorithmMenuAdapter));

        buttonUp.setOnClickListener(view -> {
            mapFragment.moveMap(0, 10);
            mapFragment.setCursorRotation(0);
        });

        buttonRight.setOnClickListener(view -> {
            mapFragment.moveMap(-10, 0);
            mapFragment.setCursorRotation(90);
        });

        buttonLeft.setOnClickListener(view -> {
            mapFragment.moveMap(10, 0);
            mapFragment.setCursorRotation(270);
        });

        buttonDown.setOnClickListener(view -> {
            mapFragment.moveMap(0, -10);
            mapFragment.setCursorRotation(180);
        });
    }

    void showAlgorithmMenuAlert(AlgorithmMenuAdapter algorithmMenuAdapter)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Choose location algorithm")
                .setAdapter(algorithmMenuAdapter, null)
                .setPositiveButton("OK", (dialog, which) -> {
                    if(algorithmMenuAdapter.isFlagSet()) {
                        algorithmMenuAdapter.setFlag(false);
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(activityContext, "Nothing was changed", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(activityContext, "Nothing was changed", Toast.LENGTH_LONG).show();
                });
        builder.show();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.mapViewFragment);
        mapFragment.setOnMapPositionChangeListener(this);
        mapFragment.setMap(R.raw.polanka_0p_10cm);
        //mapFragment.moveMap(1300, -750);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onMapPositionChange(float x, float y) {
        mapPositionTextView.setText("MAP POSITION\n" + "x: " + x + "\n" + "y: " + y);
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

    public void addNewWiFiTransmitter(String id,  double xCoord, double yCoord, double transmitPower){
        wifiTransmitterList.add(new Transmitter(id, xCoord, yCoord, transmitPower));
    }
    public void addNewBleTransmitter(String id,  double xCoord, double yCoord, double transmitPower){
        bleTransmitterList.add(new Transmitter(id, xCoord, yCoord, transmitPower));
    }
    public void deleteWifFiTransmitters()
    {
        wifiTransmitterList.clear();
    }
    public void deleteBleTransmitter()
    {
        bleTransmitterList.clear();
    }
}