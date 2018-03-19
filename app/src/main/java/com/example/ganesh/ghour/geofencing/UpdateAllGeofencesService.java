package com.example.ganesh.ghour.geofencing;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ganesh.ghour.jobscheduler.JobUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BiOs on 08-10-2017.
 */

public class UpdateAllGeofencesService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String ACTION_UPDATE_GEOFENCE = "com.android.intent.ACTION_UPDATE_GEOFENCE";
    private static final String TAG = UpdateAllGeofencesService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 12345;

    private GoogleApiClient mClient;
    private Geofencing mGeofencing;
    private DatabaseReference mDatabasePlaces;

    public UpdateAllGeofencesService() {
        super("UpdateAllGeofencesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getAction() == ACTION_UPDATE_GEOFENCE) {
            mDatabasePlaces = FirebaseDatabase.getInstance().getReference().child("placeids");
            mClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(LocationServices.API)
                    .build();
            mClient.connect();
            mGeofencing = new Geofencing(mClient, getBaseContext());
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        final List<String> guids = new ArrayList<>();

        mDatabasePlaces.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: " + child.getValue());
                    guids.add((String) child.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Cant retrieve places");
            }
        });


        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mClient,
                guids.toArray(new String[guids.size()]));
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                mGeofencing.updateAllGeofences(places);
                mGeofencing.registerAllGeofences();
            }
        });

        //Rescheduling the JOB
        Log.d("UpdateAllGeofence", "true");
        JobUtil.scheduleJob(getApplicationContext());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mClient.disconnect();
    }
}
