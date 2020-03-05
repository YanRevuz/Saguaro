package com.example.saguaro.Api;

import com.example.saguaro.Bean.Localisation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LocalisationHelper {

    private static final String COLLECTION_NAME = "Localisations";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getLocalisationsCollection(){
        System.out.println(FirebaseFirestore.getInstance().collection(COLLECTION_NAME));
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createLocalisation(String urlPicture, double logitude, double latitude) {
        Localisation LocalisationToCreate = new Localisation(urlPicture,logitude,latitude);
        return LocalisationHelper.getLocalisationsCollection().add(LocalisationToCreate);

    }

    // --- GET ---

    public static Task<DocumentSnapshot> getLocalisation(String uid){
        return LocalisationHelper.getLocalisationsCollection().document(uid).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteLocalisation(String uid) {
        return LocalisationHelper.getLocalisationsCollection().document(uid).delete();
    }
}
