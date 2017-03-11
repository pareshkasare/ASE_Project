package com.example.ved.MARS;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Ved on 3/10/2017.
 */

public class Notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra("data");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context,ReadNotification.class);
        repeating_intent.putExtra("data",data);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String[] pieces = data.split(":");

        PendingIntent pendingIntent = PendingIntent.getService(context,100,repeating_intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MARS Notification")
                .setContentText("It's time to take medicine: "+pieces[0])
                .setAutoCancel(true);

        notificationManager.notify(100,builder.build());




    }
}