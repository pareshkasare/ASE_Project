package com.example.ved.MARS;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Ved on 3/10/2017.
 */

public class Notification_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("Got broadcast from alarm");
        String data = intent.getStringExtra("data");
        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        String[] pieces = data.split(":");
        int num=Integer.parseInt(pieces[6].replaceAll("[^-?0-9]+", ""));
        String msg = "It's time to take "+pieces[0]+"\nClick Yes to confirm!";
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        data=data+":"+num;
        Intent repeating_intent = new Intent(context,ReadNotification.class);
        repeating_intent.putExtra("data",data);

        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getService(context,num,repeating_intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_action_yes,"YES",pendingIntent)
                        .build();
        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender()
                .addAction(action);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                //.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MARS Notification")
                .setContentText("It's time to take medicine: "+pieces[0])
                //.setOngoing(true)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setLights(Color.GREEN,2000,2000)
                .setSound(uri)
                .addAction(R.drawable.ic_action_yes,"YES",pendingIntent)
                .extend(extender)
                .setVibrate(new long[]{1000});

        notificationManager.notify(num,builder.build());

    }
}