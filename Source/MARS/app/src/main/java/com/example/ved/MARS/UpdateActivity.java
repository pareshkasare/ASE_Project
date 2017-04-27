package com.example.ved.MARS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringTokenizer;

public class UpdateActivity extends AppCompatActivity {

    private TextView medName,curdosage;
    private EditText dosage;
    private Spinner hours,min,ampm;
    private CheckBox sun,mon,tue,wed,thu,fri,sat;
    private boolean[] days;
    private boolean no_change,no_check;
    private String s,curContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        medName = (TextView) findViewById(R.id.medName);
        dosage  = (EditText) findViewById(R.id.dosage);
        curdosage  = (TextView) findViewById(R.id.curDosage);


        hours = (Spinner)findViewById(R.id.spinner7);
        min = (Spinner)findViewById(R.id.spinner8);
        ampm = (Spinner)findViewById(R.id.spinner9);
        sun = (CheckBox) findViewById(R.id.checkbox1);
        mon = (CheckBox) findViewById(R.id.checkbox2);
        tue = (CheckBox) findViewById(R.id.checkbox3);
        wed = (CheckBox) findViewById(R.id.checkbox4);
        thu = (CheckBox) findViewById(R.id.checkbox5);
        fri = (CheckBox) findViewById(R.id.checkbox6);
        sat = (CheckBox) findViewById(R.id.checkbox7);
        String[] items6 = new String[]{"01", "02", "03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items6);
        hours.setAdapter(adapter6);
        adapter6.notifyDataSetChanged();
        String[] items7 = new String[]{"00","05", "10","15","20","25","30","35","40","45","50","55"};
        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items7);
        min.setAdapter(adapter7);
        adapter7.notifyDataSetChanged();
        String[] items8 = new String[]{"am","pm"};
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items8);
        ampm.setAdapter(adapter8);
        adapter8.notifyDataSetChanged();

        s = getIntent().getStringExtra("Extra_filename");
        System.out.println(s);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        File med = new File(getApplicationContext().getFilesDir(),s);
        try {
            BufferedReader br = new BufferedReader(new FileReader(med));
            String line;

            while ((line = br.readLine()) != null) {
                curContent = line;
                String[] pieces = line.split(":");
                System.out.println(line);
                medName.setText(pieces[0]);
                curdosage.setText(pieces[1]);
                hours.setSelection(Arrays.asList(items6).indexOf(pieces[2]));
                min.setSelection(Arrays.asList(items7).indexOf(pieces[3]));
                ampm.setSelection(Arrays.asList(items8).indexOf(pieces[4]));
                String[] parts = pieces[5].substring(1,pieces[5].length()-1).split(", ");
                days = new boolean[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    System.out.println(parts[i]);
                    days[i] = Boolean.parseBoolean(parts[i]);
                }
                System.out.println(Arrays.toString(days));
                sun.setChecked(days[0]);
                mon.setChecked(days[1]);
                tue.setChecked(days[2]);
                wed.setChecked(days[3]);
                thu.setChecked(days[4]);
                fri.setChecked(days[5]);
                sat.setChecked(days[6]);
                //days= Arrays.asList(pieces[4].substring(1,pieces[4].length()-1).split(", "));
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Button del_button = (Button) findViewById(R.id.delete);
        del_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                delete_medicine(s);
            }
        });
        Button upd_button = (Button) findViewById(R.id.update);
        upd_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                update_medicine(s);
            }
        });
    }
    private void update_medicine(final String filename){
        no_change=false;
        no_check=false;
        new AlertDialog.Builder(this)
                .setTitle("Update Medicine")
                .setMessage("Are you sure you want to update Medicine information?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newContent = getContents();
                        if(curContent.equals(newContent)){
                            no_change = true;
                            error_note();

                        }else if(!TextUtils.isEmpty(dosage.getText().toString()) && Integer.parseInt(dosage.getText().toString()) > 365){
                            dosage.setError(getString(R.string.value_greater_365));
                            dosage.requestFocus();
                        }else if(!sun.isChecked() && !mon.isChecked() && !tue.isChecked() && !wed.isChecked() &&
                                !thu.isChecked() && !fri.isChecked() && !sat.isChecked() ) {

                            no_check=true;
                            error_note();
                        }
                        else{

                            System.out.println("current:" + curContent);
                            System.out.println("new    :" + newContent);

                            delete_alarm(filename);
                            overwrite_file(filename,newContent);
                            setupNotification(newContent,filename);

                            Intent redirect = new Intent(UpdateActivity.this, MainActivity.class);
                            redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(redirect);
                            Toast t = Toast.makeText(getApplicationContext(),"Medicine Updated!",Toast.LENGTH_SHORT);
                            t.show();

                        }
                    }

                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    private void error_note(){
        if(no_change){
            System.out.println( "no change in data");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There is no change in Medicine information")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }else if(no_check){
            System.out.println( "no checkbox selected");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("At least one day should be selected!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do nothing
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    private String getContents(){
        boolean[] days = new boolean[7];
        days[0] = sun.isChecked();
        days[1] = mon.isChecked();
        days[2] = tue.isChecked();
        days[3] = wed.isChecked();
        days[4] = thu.isChecked();
        days[5] = fri.isChecked();
        days[6] = sat.isChecked();
        System.out.println( "days: "+ Arrays.toString(days));
        String new_dosage;
        if(dosage.getText().toString().isEmpty()){
            new_dosage = curdosage.getText().toString();
        }else{
            new_dosage = dosage.getText().toString();
        }
        return medName.getText()+":"+new_dosage+":"+hours.getSelectedItem().toString()+":"+
                min.getSelectedItem().toString()+":"+ampm.getSelectedItem().toString()+
                ":"+Arrays.toString(days);


    }
    private void overwrite_file(String filename,String contents){

        File new_file = new File(getApplicationContext().getFilesDir(),filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new_file, false));
            bw.write(contents);
            System.out.println("Updated file data: "+contents);
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void delete_medicine(final String filename){
        new AlertDialog.Builder(this)
                .setTitle("Deleting Medicine")
                .setMessage("Are you sure you want to delete Medicine?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        delete_alarm(filename);
                        delete_file(filename);
                        Intent redirect = new Intent(UpdateActivity.this, MainActivity.class);
                        redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(redirect);
                        Toast t = Toast.makeText(getApplicationContext(),"Medicine Deleted!",Toast.LENGTH_SHORT);
                        t.show();
                    }

                })
                .setNegativeButton("No", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void delete_alarm(String filename){

        int intentID = Integer.parseInt(filename.replaceAll("[^-?0-9]+", ""));
        System.out.println("removing alarm if its 0 intentIDis: " + intentID);
        Intent myintent = new Intent(getApplicationContext(),Notification_receiver.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        for (int i=1;i<=7;++i) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), ((intentID * 100) + (10*i)), myintent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }
    private void delete_file(String filename){
        getApplicationContext().deleteFile(filename);
    }
    private void setupNotification(String data,String fileName){
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(hours.getSelectedItem().toString());
        int mins = Integer.parseInt(min.getSelectedItem().toString());
        System.out.println("hour: "+hour+"  mins: "+mins);
        int sec = 0;
        if(hour==12 && ampm.getSelectedItem().toString().equals("am")){
            hour-=12;
        }
        if(ampm.getSelectedItem().toString().equals("pm") && hour !=12){
            hour+=12;
        }
        System.out.println("hour: "+hour+"  mins: "+mins +"am,pm: "+ampm.getSelectedItem().toString() );
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,mins);
        calendar.set(Calendar.SECOND,sec);

        Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
        intent.putExtra("data",data+":"+fileName);

        int filenum= Integer.parseInt(fileName.replaceAll("[^-?0-9]+", ""));
        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(sun.isChecked()&&mon.isChecked()&&tue.isChecked()&&wed.isChecked()&&thu.isChecked()&&fri.isChecked()&&sat.isChecked())
        {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+10,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }
        else
        {
            if(sun.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,1);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+10,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(mon.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,2);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+20,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(tue.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,3);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+30,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(wed.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,4);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+40,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(thu.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,5);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+50,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(fri.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,6);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+60,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(sat.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,7);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),filenum*100+70,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent redirect = new Intent(UpdateActivity.this, LoginActivity.class);
                redirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(redirect);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
