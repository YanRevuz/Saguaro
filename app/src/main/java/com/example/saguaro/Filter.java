package com.example.saguaro;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Filter extends AppCompatActivity {
    private ImageView imageview;
    private Bitmap bitmap;
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    public FilterList filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_image);

        imageview = (ImageView) findViewById(R.id.imageview);
        filterList = FilterList.TRANSPARENT;

        if (bitmap == null) {
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
                    ChooseFilter(true);
                } else if (deltaX < MIN_DISTANCE) {
                    ChooseFilter(false);
                } else {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Available colors :
     * BLUE, CYAN, GRAY, GREEN, LTGRAY,
     * MAGENTA, RED, TRANSPARENT, YELLOW
     */
    public void ChooseFilter(boolean inc) {
        if (inc) {
            filterList = filterList.next();
        } else {
            filterList = filterList.previous();
        }

        switch (filterList) {
            case BLUE:
                imageview.setColorFilter(Color.BLUE, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Blue filter", Toast.LENGTH_SHORT).show();
                break;
            case CYAN:
                imageview.setColorFilter(Color.CYAN, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Cyan filter", Toast.LENGTH_SHORT).show();
                break;
            case GRAY:
                imageview.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Gray filter", Toast.LENGTH_SHORT).show();
                break;
            case GREEN:
                imageview.setColorFilter(Color.GREEN, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Green filter", Toast.LENGTH_SHORT).show();
                break;
            case LTGRAY:
                imageview.setColorFilter(Color.LTGRAY, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Lite gray filter", Toast.LENGTH_SHORT).show();
                break;
            case MAGENTA:
                imageview.setColorFilter(Color.MAGENTA, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Magenta filter", Toast.LENGTH_SHORT).show();
                break;
            case RED:
                imageview.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Red filter", Toast.LENGTH_SHORT).show();
                break;
            case YELLOW:
                imageview.setColorFilter(Color.YELLOW, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "Yellow filter", Toast.LENGTH_SHORT).show();
                break;
            default:
                imageview.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.LIGHTEN);
                Toast.makeText(this, "No filter", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
