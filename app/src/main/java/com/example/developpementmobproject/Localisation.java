package com.example.developpementmobproject;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class Localisation implements LocationListener {

    String latitude;
    String longitude;

    public String getLatitude() {
        return latitude;
    }


    public String getLongitude() {
        return longitude;
    }

    @Override
    public void onLocationChanged(Location loc) {

        this.longitude = Double.toString(loc.getLongitude());
        Log.v("a", longitude);
        this.latitude = Double.toString(loc.getLatitude());
        Log.v("a", latitude);

    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
