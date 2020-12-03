package com.pracowniatmib.indoorlocalizationsystem;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class MapFragment extends Fragment {

    private EditText xEditText;
    private EditText yEditText;
    private Button goButton;
    private ImageView cursorMarkerView;
    private FragmentManager fragmentManager;
    private MapHolderFragment mapHolderFragment;
    private OnMapPositionChangeListener onMapPositionChangeListener;
    private int cursorCurrentAngle = 0;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        cursorMarkerView = view.findViewById(R.id.cursorMarker);

        fragmentManager = getChildFragmentManager();
        mapHolderFragment = (MapHolderFragment) fragmentManager.findFragmentById(R.id.mapHolderFragment);

        xEditText = view.findViewById(R.id.inputXTextNumber);
        yEditText = view.findViewById(R.id.inputYTextNumber);
        goButton = view.findViewById(R.id.buttonGo);

        goButton.setOnClickListener(view1 -> mapHolderFragment.setMapPosition(Float.parseFloat(xEditText.getText().toString()), Float.parseFloat(yEditText.getText().toString())));

    }

    public void setMap(int resourceId)
    {
        mapHolderFragment.setMap(resourceId);
    }

    public void setMap(Bitmap mapBitmap)
    {
        mapHolderFragment.setMap(mapBitmap);
    }

    public void moveMap(float x, float y) {
        mapHolderFragment.moveMap(x, y);
        onMapPositionChangeListener.onMapPositionChange(mapHolderFragment.getMapX(), mapHolderFragment.getMapY());
    }

    public float getMapX()
    {
        return mapHolderFragment.getMapX();
    }

    public float getMapY()
    {
        return mapHolderFragment.getMapY();
    }

    public void rotateCursor(int angle) {
        RotateAnimation rotate = new RotateAnimation(cursorCurrentAngle, cursorCurrentAngle + angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(250);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        cursorMarkerView.startAnimation(rotate);
        cursorCurrentAngle = cursorCurrentAngle + angle;
    }

    public void setCursorRotation(int angle)
    {
        if(cursorCurrentAngle == angle) return;
        RotateAnimation rotate = new RotateAnimation(cursorCurrentAngle, angle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(250);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
        rotate.setInterpolator(new LinearInterpolator());
        cursorMarkerView.startAnimation(rotate);
        cursorCurrentAngle = angle;
    }

    public void setOnMapPositionChangeListener(OnMapPositionChangeListener onMapPositionChangeListener)
    {
        this.onMapPositionChangeListener = onMapPositionChangeListener;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }
}
