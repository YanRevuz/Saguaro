package com.example.saguaro.Api;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.saguaro.Bean.Localisation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class LocalisationHelper {

    private static final String COLLECTION_NAME = "Localisations";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLocalisationsCollection() {
        System.out.println(FirebaseFirestore.getInstance().collection(COLLECTION_NAME));
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createLocalisation(String urlPicture, double logitude, double latitude) {
        Localisation LocalisationToCreate = new Localisation(urlPicture, logitude, latitude);
        return LocalisationHelper.getLocalisationsCollection().add(LocalisationToCreate);

    }

    // --- GET ---

    public static Task<DocumentSnapshot> getLocalisation(String uid) {
        return LocalisationHelper.getLocalisationsCollection().document(uid).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteLocalisation(String uid) {
        return LocalisationHelper.getLocalisationsCollection().document(uid).delete();
    }

    //-- GET List img --
    public static void findAll() {
        LocalisationHelper.getLocalisationsCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<DocumentSnapshot> myListOfDocuments;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    myListOfDocuments = task.getResult().getDocuments();
                }
                for(DocumentSnapshot doc : myListOfDocuments) {
                    System.out.println("DOOOOOOOOOOOOOOOOOOC : " );
                    System.out.println(doc.getData());
                    System.out.println();
                }
            }
        });
    }
    static byte[] myPicture;

    public static byte[] getOneImage(String uri){

         FirebaseStorage.getInstance().getReference().child(uri).getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
               myPicture = bytes;
            }
        });
        return myPicture;
    }
}
