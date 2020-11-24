package com.pracowniatmib.indoorlocalizationsystem;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;

import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MapHolderFragment extends Fragment {

    private ImageView mapView;
    private ConstraintLayout constraintLayout;
    private ArrayList<View> viewList;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        constraintLayout = view.findViewById(R.id.mapHolderConstraintLayout);
        mapView = view.findViewById(R.id.mapView);
        mapView.setImageResource(R.drawable.default_indoor_map);

        //TODO: when creating a new ImageView on the map, we need to add it to the viewList
        //TODO: when deleting an ImageView from the map, we need to remove it from the viewList
        viewList = getAllViews(constraintLayout);
    }

    private ArrayList<View> getAllViews(ConstraintLayout constraintLayout) {
        ArrayList<View> viewList = new ArrayList<>();
        for(int i=0; i<constraintLayout.getChildCount(); i++)
        {
            viewList.add(constraintLayout.getChildAt(i));
        }
        return viewList;
    }

    public void setMap(int resourceId)
    {
        mapView.setImageResource(resourceId);
    }

    public void moveMap(float x, float y) {
        for(View view : viewList)
        {
            moveView(view, x, y);
        }
    }

    public void moveView(View view, float x, float y)
    {
        Path path = new Path();
        path.moveTo(view.getX() + x, view.getY() + y);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_holder, container, false);
    }

    public MapHolderFragment() {
        // Required empty public constructor
    }
}