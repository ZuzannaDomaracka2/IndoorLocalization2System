package com.pracowniatmib.indoorlocalizationsystem;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class MapFragment extends Fragment {

    private ImageView mapView;
    private ImageView cursorMarkerView;
    private int cursorCurrentAngle = 0;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        cursorMarkerView = view.findViewById(R.id.cursorMarker);
        mapView.setImageResource(R.drawable.default_indoor_map);
    }

    public void setMap(int resourceId)
    {
        mapView.setImageResource(resourceId);
    }

    public void moveMap(float x, float y) {
        Path path = new Path();
        path.moveTo(mapView.getX() + x, mapView.getY() + y);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mapView, View.X, View.Y, path);
        animator.start();
    }

    public float getMapX()
    {
        return mapView.getX();
    }

    public float getMapY()
    {
        return mapView.getY();
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
