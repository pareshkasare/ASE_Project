package com.example.ved.MARS;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by Ved on 3/10/2017.
 */

public class ReadNotification extends IntentService {

    public ReadNotification() {
        super("MyTestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("READNOTIFICATION", "Service running");

        Log.i(TAG, "In nofityMessage class on create");
        String data = intent.getStringExtra("data");
        String[] pieces = data.split(":");
        Log.i(TAG, "Data: " + data);
        Log.i(TAG, "filename for updation: " + pieces[6]);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(Integer.parseInt(pieces[7]));


            File med_file = new File(getApplicationContext().getFilesDir(), pieces[6]);
            if(med_file.exists()) {

                //Read text from file
                try {

                    StringBuilder text = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(med_file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            System.out.println("Current Line: "+line);
                            text.append(line);
                        }

                        br.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FileWriter medfilestream = new FileWriter(med_file, false);
                    String s = text.toString();
                    System.out.println("data read from file");
                    System.out.println(s);
                    String[] content = s.split(":");
                    s= content[0]+":"+(Integer.parseInt(content[1]) -1)+":" +content[2]+":"+content[3]+":"+content[4]+":"+content[5];

                    medfilestream.write(s);
                    medfilestream.close();

                    System.out.println("Changed content: " + s);
                    if(Integer.parseInt(content[1]) -1 == 0){


                        int intentID = Integer.parseInt(pieces[6].replaceAll("[^-?0-9]+", ""));
                        System.out.println("removing alarm if its 0 intentIDis: " + intentID);
                        Intent myintent = new Intent(getApplicationContext(),Notification_receiver.class);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        for (int i=1;i<=7;++i) {
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ((intentID * 100) + (10*i)), myintent, PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntent);
                            pendingIntent.cancel();
                        }
                        System.out.println("cancelled intent and alarm");
                        createalert(pieces[0]);
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }else
            {
                System.out.println("no file found");
                 }




    }
    public void createalert(String medname){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("MARS Refill Notification")
                .setAutoCancel(true)
                .setContentText("[QTY 0] It's time to refill medicine: "+medname)
                .setLights(Color.GREEN,2000,2000)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("[QTY 0] It's time to refill medicine: "+medname))
                .setSound(uri)
                //.extend(
                 //       new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                .setVibrate(new long[]{1000});

        notificationManager.notify(9999,builder.build());
    }
}
