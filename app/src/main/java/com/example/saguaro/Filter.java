package com.example.saguaro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saguaro.Api.LocalisationHelper;
import com.example.saguaro.Services.Position;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.xiaopo.flying.sticker.BitmapStickerIcon;
import com.xiaopo.flying.sticker.DeleteIconEvent;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.FlipHorizontallyEvent;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.xiaopo.flying.sticker.ZoomIconEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class Filter extends AppCompatActivity {
    private ImageView imageview;

    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    public FilterList filterList;
    FloatingActionButton saveImageButton, addicon;
    Bitmap bitmapOrigine;
    private Position positionService;
    private static final String TAG = MainActivity.class.getSimpleName();
    private StickerView stickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fetch_image);
        positionService = new Position(Filter.this);
        imageview = (ImageView) findViewById(R.id.imageview);

        stickerView = findViewById(R.id.sticker_view);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        filterList = FilterList.TRANSPARENT;
        if (bitmapOrigine == null) {
            try {
                bitmapOrigine = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(getIntent().getStringExtra("MonImage")));
                imageview.setImageBitmap(bitmapOrigine);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Filter.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
        saveImageButton = findViewById(R.id.saveImage);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                stickerView.createBitmap().compress(Bitmap.CompressFormat.JPEG, 100, stream);
                uploadPhotoInFirebaseAndSendLocalisation(stream.toByteArray());
            }
        });

        ////GESTION STICKER

        addicon = findViewById(R.id.addicon);
        addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadSticker();
            }
        });

        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                com.xiaopo.flying.sticker.R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());


        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));

        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);


        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                if (sticker instanceof TextSticker) {
                    ((TextSticker) sticker).setTextColor(Color.RED);
                    stickerView.replace(sticker);
                    stickerView.invalidate();
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }


            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });

    }

    private void loadSticker() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_launcher_background);
        stickerView.addSticker(new DrawableSticker(drawable));
    }

    protected OnFailureListener onFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
            }

        };
    }

    private void uploadPhotoInFirebaseAndSendLocalisation(byte[] bytes) {
        String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
        // A - UPLOAD TO GCS
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putBytes(bytes).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String pathImageSavedInFirebase = taskSnapshot.getMetadata().getPath().toString();
                // B - SAVE MESSAGE IN FIRESTORE
                Location location = positionService.getLocation();
                System.out.println(location.toString() + " GNEUUUUUUUUUUU");
                LocalisationHelper.createLocalisation(pathImageSavedInFirebase, location.getLongitude(), location.getLatitude()).addOnFailureListener(onFailureListener());
            }
        })
                .addOnFailureListener(this.onFailureListener());
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
    public Bitmap ChooseFilter(boolean inc) {
        BitmapDrawable drawable;
        Bitmap currentFilter;
        if (inc) {
            filterList = filterList.next();
        } else {
            filterList = filterList.previous();
        }
        ColorMatrix matrix;
        switch (filterList) {
            case BLUE:
                matrix = new ColorMatrix(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0});
                drawable = (BitmapDrawable) imageview.getDrawable();
                currentFilter = drawable.getBitmap();
                Toast.makeText(this, "Blue filter", Toast.LENGTH_SHORT).show();
                break;
            case GRAY:
                matrix = new ColorMatrix(new float[]{(float) 0.33, (float) 0.33, (float) 0.33, 0, 0, (float) 0.33, (float) 0.33, (float) 0.33, 0, 0, (float) 0.33, (float) 0.33, (float) 0.33, 0, 0, 0, 0, 0, 1, 0});
                drawable = (BitmapDrawable) imageview.getDrawable();
                currentFilter = drawable.getBitmap();
                Toast.makeText(this, "Gray filter", Toast.LENGTH_SHORT).show();
                break;
            case GREEN:
                matrix = new ColorMatrix(new float[]{0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0});
                drawable = (BitmapDrawable) imageview.getDrawable();
                currentFilter = drawable.getBitmap();
                Toast.makeText(this, "Green filter", Toast.LENGTH_SHORT).show();
                break;
            case RED:
                matrix = new ColorMatrix(new float[]{1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0});
                drawable = (BitmapDrawable) imageview.getDrawable();
                currentFilter = drawable.getBitmap();
                Toast.makeText(this, "Red filter", Toast.LENGTH_SHORT).show();
                break;
            default:
                matrix = new ColorMatrix(new float[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0});
                currentFilter = bitmapOrigine;
                Toast.makeText(this, "No filter", Toast.LENGTH_SHORT).show();
                break;
        }

        Bitmap bitmap = Bitmap.createBitmap(currentFilter.getWidth(), currentFilter.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(currentFilter, 0, 0, paint);
        imageview.setImageBitmap(bitmap);

        return bitmap;
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1410);
    }
}
