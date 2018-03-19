package com.example.ganesh.ghour.geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiOs on 07-10-2017.
 */

public class Geofencing implements ResultCallback {

    private static final float GEOFENCE_RADIUS = 150f;
    private static final String TAG = Geofencing.class.getSimpleName();

    public GoogleApiClient mGoogleClient;
    public Context mContext;
    public List<Geofence> mListOfGeofences;
    public PendingIntent mGeofenceIntent;

    public Geofencing(GoogleApiClient mGoogleClient, Context mContext) {
        this.mGoogleClient = mGoogleClient;
        this.mContext = mContext;
        mListOfGeofences = new ArrayList<>();
        mGeofenceIntent = null;
    }

    public void updateAllGeofences(PlaceBuffer places) {

        if (places == null || places.getCount() == 0) {
            return;
        }

        for (Place place : places) {
            String placeId = place.getId();
            double lat = place.getLatLng().latitude;
            double lng = place.getLatLng().longitude;

            Geofence.Builder builder = new Geofence.Builder()
                    .setRequestId(placeId)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .setCircularRegion(lat, lng, GEOFENCE_RADIUS)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE);
            mListOfGeofences.add(builder.build());
        }

    }

    public void registerAllGeofences() {
        if (mGoogleClient == null || !mGoogleClient.isConnected()) {
            return;
        }
        try {
            LocationServices.GeofencingApi.addGeofences(mGoogleClient, getGeofencingRequest(), getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    public void unregisterAllGeofences() {
        if (mGoogleClient == null || !mGoogleClient.isConnected()) {
            return;
        }
        try {
            LocationServices.GeofencingApi.removeGeofences(mGoogleClient, getGeofencePendingIntent()
            ).setResultCallback(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public PendingIntent getGeofencePendingIntent() {
        if (mGeofenceIntent != null) {
            return mGeofenceIntent;
        }
        Intent intent = new Intent(mContext, GeofencingBroadcastReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pIntent;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mListOfGeofences);
        return builder.build();
    }

    @Override
    public void onResult(@NonNull Result result) {
        Log.d(TAG, "Error adding/removing geofence");
    }
}
