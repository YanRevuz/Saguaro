package com.example.saguaro.Api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.example.saguaro.Bean.Localisation;
import com.example.saguaro.MapsActivity;
import com.example.saguaro.MarkerCustom;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class LocalisationHelper {

    private static final String COLLECTION_NAME = "Localisations";

    // --- COLLECTION REFERENCE ---
    static byte[] myPicture;

    // --- CREATE ---

    public static CollectionReference getLocalisationsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- GET ---

    public static Task<DocumentReference> createLocalisation(String urlPicture, double longitude, double latitude) {
        Localisation LocalisationToCreate = new Localisation(urlPicture, longitude, latitude);
        return LocalisationHelper.getLocalisationsCollection().add(LocalisationToCreate);

    }

    // --- DELETE ---

    public static Task<DocumentSnapshot> getLocalisation(String uid) {
        return LocalisationHelper.getLocalisationsCollection().document(uid).get();
    }

    public static Task<Void> deleteLocalisation(String uid) {
        return LocalisationHelper.getLocalisationsCollection().document(uid).delete();
    }


    //-- GET List img --
    public static void findAll(final GoogleMap mMap, final Context context) {
        final List<Localisation> res = new ArrayList<>();
        LocalisationHelper.getLocalisationsCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<DocumentSnapshot> myListOfDocuments;

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myListOfDocuments = task.getResult().getDocuments();
                }
                for (DocumentSnapshot doc : myListOfDocuments) {
                    // url , longitude , latitude
                    Localisation l = new Localisation(doc.getData().values().toArray()[2].toString(),
                            Double.parseDouble(doc.getData().values().toArray()[3].toString()),
                            Double.parseDouble(doc.getData().values().toArray()[1].toString()));
                    res.add(l);
                }

                MapsActivity.callbackFirebase(res, mMap, context);
            }
        });
    }

    public static void getOneImage(final GoogleMap mMap, final Localisation localisation, final Context context) {
        FirebaseStorage.getInstance().getReference().child(localisation.getUrlPicture()).getBytes(1024 * 1024 * 10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                myPicture = bytes;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                MarkerCustom markerCustom = new MarkerCustom(context);
                mMap.setInfoWindowAdapter(markerCustom);
                LatLng location = new LatLng(localisation.getLatitude(), localisation.getLongitude());
                Marker m = mMap.addMarker(new MarkerOptions().position(location));
                m.setTag(bitmap);
            }
        });
    }
}
