package com.example.saguaro.Localisation;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

public class Position {

    private String fournisseur;
    private LocationManager locationManager;
    private Activity activity;

    public Position(Activity act) {
        this.activity = act;
    }

    public Location getProvider() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria critere = new Criteria();

// Pour indiquer la précision voulue
// On peut mettre ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision
        critere.setAccuracy(Criteria.ACCURACY_COARSE);

// Est-ce que le fournisseur doit être capable de donner une altitude ?
        critere.setAltitudeRequired(false);

// Est-ce que le fournisseur doit être capable de donner une direction ?
        critere.setBearingRequired(false);

// Est-ce que le fournisseur peut être payant ?
        critere.setCostAllowed(false);

// Pour indiquer la consommation d'énergie demandée
// Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
        critere.setPowerRequirement(Criteria.POWER_MEDIUM);

// Est-ce que le fournisseur doit être capable de donner une vitesse ?
        critere.setSpeedRequired(false);

        fournisseur = locationManager.getBestProvider(critere, true);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
//           new AlertDialog.Builder(activity).setMessage("R string request permission").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//               @Override
//               public void onClick(DialogInterface dialogInterface, int i) {
//                   ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
//               }
//           });
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        return locationManager.getLastKnownLocation(fournisseur);
    }


}
