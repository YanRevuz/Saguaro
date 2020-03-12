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
    private Bitmap bitmap;

    public MarkerCustom(Context context, Bitmap bitmap) {
        this.context = context;
        this.bitmap = bitmap;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.marker_custom, null);

        ImageView imgview = view.findViewById(R.id.imageView);
        imgview.setImageBitmap(bitmap);
        return view;
    }
}
