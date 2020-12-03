package com.pracowniatmib.indoorlocalizationsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapPositionChangeListener {
    Context activityContext = this;
    MyApplication myApplication;

    private List<Transmitter> wifiTransmitterList;
    private List<Transmitter> bleTransmitterList;
    private List<User> userList;

    String userId;
    Bitmap currentMapBitmap;
    boolean newMapReady;
    DatabaseReference dataBuildings;
    DatabaseReference dataUsers;
    StorageReference storageReference;

    FragmentManager fragmentManager;
    MapFragment mapFragment;
    FragmentContainerView mapFragmentContainerView;
    ProgressBar mapViewProgressBar;

    TextView mapPositionTextView;
    TextView debugTextView;
    Button buttonSettings;
    Button buttonUpdateMap;
    Button buttonAlgorithmMode;
    Button buttonUp;
    Button buttonRight;
    Button buttonLeft;
    Button buttonDown;

    AlgorithmMenuAdapter algorithmMenuAdapter;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Objects.requireNonNull(getSupportActionBar()).hide();
        myApplication = (MyApplication) getApplication();

        //initialize object lists
        wifiTransmitterList = new ArrayList<>();
        bleTransmitterList = new ArrayList<>();
        userList = new ArrayList<>();

        //fetch the algorithm option list from Application-scope variable
        ArrayList<AlgorithmOption> algorithmOptionList = myApplication.getAlgorithmOptionList();
        algorithmMenuAdapter = new AlgorithmMenuAdapter(activityContext, algorithmOptionList);

        //initialize utility objects
        fragmentManager = getSupportFragmentManager();
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        //get handles to UI objects
        mapFragmentContainerView = findViewById(R.id.mapViewFragment);
        mapViewProgressBar = findViewById(R.id.mapViewProgressBar);
        mapPositionTextView = findViewById(R.id.mapPositionTextView);
        debugTextView = findViewById(R.id.textDebugMap);
        buttonSettings = findViewById(R.id.buttonSettingsMap);
        buttonUpdateMap = findViewById(R.id.buttonUpdateMap);
        buttonAlgorithmMode = findViewById(R.id.buttonAlgorithmModeMap);
        buttonUp = findViewById(R.id.buttonUp);
        buttonRight = findViewById(R.id.buttonRight);
        buttonLeft = findViewById(R.id.buttonLeft);
        buttonDown = findViewById(R.id.buttonDown);

        //set button OnClickListeners
        buttonSettings.setOnClickListener(view -> Toast.makeText(MapActivity.this, "SETTINGS BUTTON CLICKED!", Toast.LENGTH_SHORT).show());

        buttonUpdateMap.setOnClickListener(view -> showMapUpdateMenuAlert());

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


        //read/write from/to database
        currentMapBitmap = downloadMap("WiIT_Polanka", "0");
        dataBuildings = FirebaseDatabase.getInstance().getReference("Buildings");
        dataUsers = FirebaseDatabase.getInstance().getReference("Users");
        userId = dataUsers.push().getKey();
    }

    private void showMapUpdateMenuAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activityContext)
                .setCancelable(false)
                .setTitle("Choose map to download from database");;

        View inflatedView = LayoutInflater.from(activityContext).inflate(R.layout.map_update_menu_alert, (ViewGroup) findViewById(R.id.content));
        final EditText mapNameEditText = inflatedView.findViewById(R.id.mapNameTextView);
        final EditText floorNoEditText = inflatedView.findViewById(R.id.floorNoTextView);
        builder.setView(inflatedView);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mapNameEditText.getText().toString().trim().isEmpty() || floorNoEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(activityContext, "Fill in the map name and floor number!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateMap(mapNameEditText.getText().toString().trim(), floorNoEditText.getText().toString().trim());
                    dialogInterface.dismiss();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void updateMap(String mapName, String floorNo) {
        mapFragmentContainerView.setVisibility(View.INVISIBLE);
        mapViewProgressBar.setVisibility(View.VISIBLE);

        currentMapBitmap = downloadMap(mapName, floorNo);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(newMapReady)
                {
                    mapFragment.setMap(currentMapBitmap);
                    mapFragmentContainerView.setVisibility(View.VISIBLE);
                    mapViewProgressBar.setVisibility(View.GONE);
                }
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    void showAlgorithmMenuAlert(AlgorithmMenuAdapter algorithmMenuAdapter)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activityContext)
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

    public Bitmap downloadMap(String mapName, String floor)
    {
        newMapReady = false;
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference mapRef = storageReference.child(mapName + "_" + floor +".bmp");

        final Bitmap[] mapBitmap = new Bitmap[1];

        final long TWO_MEGABYTES = 2 * 1024 * 1024;
        mapRef.getBytes(TWO_MEGABYTES).addOnSuccessListener(bytes -> {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            mapBitmap[0] = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length, options);
            newMapReady = true;
        }).addOnFailureListener(exception -> {
            int errorCode = ((StorageException) exception).getErrorCode();
            String errorMessage = exception.getMessage();
            Log.d("TAG", errorMessage + errorCode);
        });

        return mapBitmap[0];
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

    @Override
    protected void onStop() {
        super.onStop();
        //delete user database record
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userReference.removeValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApplication.setAlgorithmOptionList((ArrayList<AlgorithmOption>) algorithmMenuAdapter.getAlgorithmOptionList());
    }
}