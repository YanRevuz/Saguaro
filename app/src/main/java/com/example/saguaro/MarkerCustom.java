package com.example.saguaro;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MarkerCustom implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public MarkerCustom(Context context) {
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.marker_custom, null);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap((Bitmap) marker.getTag());
        return view;
    }
}
