package com.example.saguaro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mukesh.image_processing.ImageProcessor;

public class Filter extends AppCompatActivity {
    private ImageView imageview;
    private static final String IMAGE_DIRECTORY = "/SaguanoPicture";
    private int GALLERY = 1, CAMERA = 2;
    private Bitmap bitmap;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;

    public static enum FilterList {
        Brightness,
        Contrast,
        CrossProcess,
        Documentary,
        DuoTone,
        Fill_Light,
        FishEye,
        Flip_Horizontally,
        Flip_Vertically,
        Grain,
        Grayscale,
        Highlight,
        Lomoish,
        Negative,
        None,
        Posterize,
        Rotate,
        Saturate,
        Sepia,
        Sharpen,
        Temperature,
        Tint,
        Vignette;
        private static FilterList[] vals = values();

        public FilterList next() {
            return vals[(this.ordinal() + 1) % vals.length];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_image);

        imageview = (ImageView) findViewById(R.id.imageview);

        try {

            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(getIntent().getStringExtra("MonImage")));
            //  String path = saveImage(bitmap);
            Toast.makeText(Filter.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            imageview.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Filter.this, "Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPicture() {

    }





    /**
     * Detect swipe left to right or right to left
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (deltaX > MIN_DISTANCE) {
                    Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();

                   // bitmap = BitmapFactory.decodeResource(getResources(), R.id.imageview);
                    ImageProcessor imageProcessor =  new ImageProcessor();
                    bitmap  = imageProcessor.doInvert(bitmap);
                    imageview = (ImageView) findViewById(R.id.imageview);
                    imageview.setImageBitmap(bitmap);

                } else if (deltaX < MIN_DISTANCE) {
                    Toast.makeText(this, "right2left swipe", Toast.LENGTH_SHORT).show();
                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
