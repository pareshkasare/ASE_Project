package com.example.ved.MARS;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        String filename = intent.getStringExtra("filename");
        Log.i(TAG, "Data: " + data);
        Log.i(TAG, "filename for updation: " + filename);
        /*
            File user_file = new File(getApplicationContext().getFilesDir(), "test.txt");
            if(user_file.exists()) {

                //Read text from file
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(user_file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                    }
                    Log.i(TAG, "TEXT: " + text.toString());
                    br.close();
                    user_file.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {
                System.out.println("no file found");
                Log.i(TAG, "file test.txt not found");

            }

*/


    }
}
