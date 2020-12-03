package com.pracowniatmib.indoorlocalizationsystem;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MapHolderFragment extends Fragment {

    private ImageView mapView;
    private ConstraintLayout constraintLayout;
    private ArrayList<View> viewList;
    private TextView debugTextView;

    private int currentMapImageResId;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        constraintLayout = view.findViewById(R.id.mapHolderConstraintLayout);
        mapView = view.findViewById(R.id.mapView);
        debugTextView = view.findViewById(R.id.debugMapHolderTextView);
        setMap(R.drawable.default_indoor_map);

        //TODO: when creating a new ImageView on the map, we need to add it to the viewList
        //TODO: when deleting an ImageView from the map, we need to remove it from the viewList
        viewList = getAllViews(constraintLayout);
    }

    private void updateScale() {
        float mapViewWidth = mapView.getWidth();
        float mapViewHeight = mapView.getHeight();
        float measuredMapViewWidth = mapView.getMeasuredWidth();
        float measuredMapViewHeight = mapView.getMeasuredHeight();
        int[] currentMapImageActualDimensions = getCurrentMapImageActualDimensions();
        float actualMapImageWidth = currentMapImageActualDimensions[0];
        float actualMapImageHeight = currentMapImageActualDimensions[1];
        float scaledMapImageWidth = ResourcesCompat.getDrawable(getResources(), currentMapImageResId, null).getIntrinsicWidth();
        float scaledMapImageHeight = ResourcesCompat.getDrawable(getResources(), currentMapImageResId, null).getIntrinsicHeight();
        float mapScale = mapView.getScaleX();
        debugTextView.setText("mapViewWidth = " + mapViewWidth + "\n" +
                            "mapViewHeight = " + mapViewHeight + "\n" +
                            "measuredMapViewWidth = " + measuredMapViewWidth + "\n" +
                            "measuredMapViewHeight = " + measuredMapViewHeight + "\n" +
                            "actualMapImageWidth = " + actualMapImageWidth + "\n" +
                            "actualMapImageHeight = " + actualMapImageHeight + "\n" +
                            "scaledMapImageWidth = " + scaledMapImageWidth + "\n" +
                            "scaledMapImageHeight = " + scaledMapImageHeight + "\n" +
                            "mapScale = " + mapScale + "\n" +
                            "finalMapImageWidth = " + mapScale * scaledMapImageWidth + "\n" +
                            "finalMapImageHeight = " + mapScale * scaledMapImageHeight + "\n" +
                            "mapView's imageMatrix: " + mapView.getImageMatrix().toString());
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
        currentMapImageResId = resourceId;
        updateScale();
    }

    private int[] getCurrentMapImageActualDimensions()
    {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), currentMapImageResId, o);
        int width = o.outWidth;
        int height = o.outHeight;
        return new int[]{width, height};
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
        updateScale();
    }

    public void setViewPosition(View view, float x, float y)
    {
        Path path = new Path();
        path.moveTo(x, y);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.start();
        updateScale();
    }

    public void setMapPosition(float x, float y)
    {
        for(View view : viewList)
        {
            setViewPosition(view, x, y);
        }
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