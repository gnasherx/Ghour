package com.example.ganesh.ghour.geofencing;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;

/**
 * Created by BiOs on 07-10-2017.
 */

public class CheckAllGeofences extends com.firebase.jobdispatcher.JobService {

    public CheckAllGeofences() {
        super();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Toast.makeText(this, "Job Started", Toast.LENGTH_LONG).show();
        Log.d("CheckAllGeofences", "onStartJob");
        Intent intent  = new Intent(getApplicationContext(),  UpdateAllGeofencesService.class);
        intent.setAction(UpdateAllGeofencesService.ACTION_UPDATE_GEOFENCE);
        startService(intent);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Toast.makeText(this, "onStopJob", Toast.LENGTH_LONG).show();
        return true;
    }
}
