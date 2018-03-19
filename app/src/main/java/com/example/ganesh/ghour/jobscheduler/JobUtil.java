package com.example.ganesh.ghour.jobscheduler;

import android.content.Context;
import android.widget.Toast;

import com.example.ganesh.ghour.geofencing.CheckAllGeofences;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by BiOs on 07-10-2017.
 */

public class JobUtil {

    public static void scheduleJob(Context context) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job myJob = dispatcher.newJobBuilder()
                .setService(CheckAllGeofences.class)
                .setTag("check-all-geofences")
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRecurring(false)
                .setTrigger(Trigger.executionWindow(60*15, 60*20))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(myJob);
        Toast.makeText(context, "Job Scheduled", Toast.LENGTH_LONG).show();
    }

}
