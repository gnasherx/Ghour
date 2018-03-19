package com.example.ganesh.ghour.geofencing;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.ganesh.ghour.MainActivity;
import com.example.ganesh.ghour.R;

/**
 * Created by BiOs on 07-10-2017.
 */

public class GeofencingBroadcastReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 139;

    @Override
    public void onReceive(Context context, Intent intent) {
        //ALl the things we have to do when Geofencing is encountered

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentInfo("Your neighbors need your help!!!")
                .setContentTitle("Emergency")
                .setContentText("You can help someone save their life around you, right now.")
                .setContentIntent(pIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_ID, builder.build());
    }

}
