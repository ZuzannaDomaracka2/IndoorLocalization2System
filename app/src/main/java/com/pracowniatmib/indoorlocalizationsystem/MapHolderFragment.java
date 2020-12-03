package com.pracowniatmib.indoorlocalizationsystem;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

    int[] currentMapImageActualDimensions;
    float actualMapImageWidth, actualMapImageHeight;
    float scaledMapImageWidth, scaledMapImageHeight;
    float mapScale;
    float[] imageMatrix = new float[9];
    float beginCoordX, beginCoordY;

    private int currentMapImageResId;
    private Bitmap currentMapBitmap;

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
        currentMapImageActualDimensions = getCurrentMapImageActualDimensions();
        actualMapImageWidth = currentMapImageActualDimensions[0];
        actualMapImageHeight = currentMapImageActualDimensions[1];
        mapScale = mapView.getScaleX();
        if (mapView.getImageMatrix() != null) mapView.getImageMatrix().getValues(imageMatrix);
        if (currentMapImageResId != 0)
        {
            scaledMapImageWidth = ResourcesCompat.getDrawable(getResources(), currentMapImageResId, null).getIntrinsicWidth();
            scaledMapImageHeight = ResourcesCompat.getDrawable(getResources(), currentMapImageResId, null).getIntrinsicHeight();
        }
        else
        {
            scaledMapImageWidth = Math.round(currentMapBitmap.getWidth() * getResources().getDisplayMetrics().density);
            scaledMapImageHeight = Math.round(currentMapBitmap.getHeight() * getResources().getDisplayMetrics().density);
        }
        beginCoordX = (actualMapImageWidth * imageMatrix[Matrix.MSCALE_X] * mapScale)/2;
        beginCoordY = (actualMapImageHeight * imageMatrix[Matrix.MSCALE_Y] * mapScale)/2;
        debugTextView.setText("mapViewWidth = " + mapViewWidth + "\n" +
                            "mapViewHeight = " + mapViewHeight + "\n" +
                            "measuredMapViewWidth = " + measuredMapViewWidth + "\n" +
                            "measuredMapViewHeight = " + measuredMapViewHeight + "\n" +
                            "actualMapImageWidth = " + actualMapImageWidth + "\n" +
                            "actualMapImageHeight = " + actualMapImageHeight + "\n" +
                            "scaledMapImageWidth = " + scaledMapImageWidth + "\n" +
                            "scaledMapImageHeight = " + scaledMapImageHeight + "\n" +
                            "mapScale = " + mapScale + "\n" +
                            "MSCALE_X = " + imageMatrix[Matrix.MSCALE_X] + "\n" +
                            "MSCALE_Y = " + imageMatrix[Matrix.MSCALE_Y] + "\n" +
                            "beginCoordX = " + beginCoordX + "\n" +
                            "beginCoordY = " + beginCoordY + "\n" +
                            "imageMatrix: " + mapView.getImageMatrix().toString());
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
        currentMapBitmap = null;
        updateScale();
    }

    public void setMap(Bitmap mapBitmap)
    {
        mapView.setImageBitmap(mapBitmap);
        currentMapBitmap = mapBitmap;
        currentMapImageResId = 0;
        updateScale();
    }

    private int[] getCurrentMapImageActualDimensions()
    {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        int width, height;
        if(currentMapImageResId != 0)
        {
            Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), currentMapImageResId, o);
            width = o.outWidth;
            height = o.outHeight;
        }
        else
        {
            width = currentMapBitmap.getWidth();
            height = currentMapBitmap.getHeight();
        }
        return new int[]{width, height};
    }

    private float[] translateCoordinates(float xImage, float yImage)
    {
        float[] ui_coordinates = new float[2];
        ui_coordinates[0] = beginCoordX + xImage * mapScale * imageMatrix[Matrix.MSCALE_X];
        ui_coordinates[1] = beginCoordY + yImage * mapScale * imageMatrix[Matrix.MSCALE_Y];
        return ui_coordinates;
    }

    private float[] reverseTranslateCoordinates(float xUi, float yUi)
    {
        float[] image_coordinates = new float[2];
        image_coordinates[0] =  (beginCoordX - xUi) / (mapScale * imageMatrix[Matrix.MSCALE_X]);
        image_coordinates[1] =  (beginCoordY - yUi) / (mapScale * imageMatrix[Matrix.MSCALE_Y]);
        return image_coordinates;
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

    public void setViewPosition(View view, float x, float y)
    {
        Path path = new Path();
        path.moveTo(x, y);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.start();
    }

    public void setMapPosition(float x, float y)
    {
        for(View view : viewList)
        {
            setViewPosition(view, x, y);
        }
    }

    public float getUiMapX()
    {
        return mapView.getX();
    }

    public float getUiMapY()
    {
        return mapView.getY();
    }

    public float getImageMapX()
    {
        return Math.round(reverseTranslateCoordinates(mapView.getX(), mapView.getY())[0]);
    }

    public float getImageMapY()
    {
        return Math.round(reverseTranslateCoordinates(mapView.getX(), mapView.getY())[1]);
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