package com.example.ved.MARS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private EditText medname;
    private EditText dosage;
    private CheckBox ck1, ck2, ck3, ck4, ck5, ck6, ck7;
    private Button save;
    private TextView textView5, textView6, textView7;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner1, spinner4, spinner5, spinner6, spinner7, spinner8, spinner9;
    private Spinner dropdown,dropdown1,dropdown2,
            dropdown3,dropdown4,dropdown5,dropdown6,
            dropdown7,dropdown8,dropdown9;
    private boolean isInputValid = false;
    private boolean [] days = new boolean[7];
    String data,fileName;

    public void AddActivity_user(){
        System.out.println("in add user");
        medname =  (EditText) findViewById(R.id.textView4);
        dosage = (EditText) findViewById(R.id.textView2);
        ck1 = (CheckBox) findViewById(R.id.checkbox1);
        ck2 = (CheckBox) findViewById(R.id.checkbox2);
        ck3 = (CheckBox) findViewById(R.id.checkbox3);
        ck4 = (CheckBox) findViewById(R.id.checkbox4);
        ck5 = (CheckBox) findViewById(R.id.checkbox5);
        ck6 = (CheckBox) findViewById(R.id.checkbox6);
        ck7 = (CheckBox) findViewById(R.id.checkbox7);


        String  Medname= medname.getText().toString();
        String Dosage = dosage.getText().toString();

        if (TextUtils.isEmpty(Medname)) {
            medname.setError(getString(R.string.error_field_required));
            medname.requestFocus();
        } else if (TextUtils.isEmpty(Dosage)) {
            dosage.setError(getString(R.string.error_field_required));
            dosage.requestFocus();
        }else if(!ck1.isChecked() && !ck2.isChecked() && !ck3.isChecked() && !ck4.isChecked() &&
                !ck5.isChecked() && !ck6.isChecked() && !ck7.isChecked() ) {

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
        else
        {
            isInputValid = true;
        }

        if(isInputValid){
            days[0] = ck1.isChecked();
            days[1] = ck2.isChecked();
            days[2] = ck3.isChecked();
            days[3] = ck4.isChecked();
            days[4] = ck5.isChecked();
            days[5] = ck6.isChecked();
            days[6] = ck7.isChecked();

            data = Medname+":"+Dosage+":"+dropdown.getSelectedItem().toString()+
                    dropdown1.getSelectedItem().toString()+dropdown2.getSelectedItem().toString()+":"
                    +dropdown3.getSelectedItem().toString()+
                    dropdown4.getSelectedItem().toString()+dropdown5.getSelectedItem().toString()+":"
                    +dropdown6.getSelectedItem().toString()+":"+
                    dropdown7.getSelectedItem().toString()+":"+dropdown8.getSelectedItem().toString()+
                    days.toString();
            System.out.println(data);
            //check if users file already present, if not then create one. If present then append data.
            fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
            File user_file = new File(getApplicationContext().getFilesDir(), "Med"+fileName+".txt");
            if(!user_file.exists()){
                create_new_file(data,user_file);
            }

            setupNotification();

        }

    }
    private void setupNotification(){
        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(dropdown6.getSelectedItem().toString());
        int mins = Integer.parseInt(dropdown7.getSelectedItem().toString());
        System.out.println("hour: "+hour+"  mins: "+mins);
        int sec = 00;
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,mins);
        calendar.set(Calendar.SECOND,sec);

        Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
        intent.putExtra("data",data);
        intent.putExtra("filename","Med"+fileName+".txt");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent. FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }
    private void create_new_file(String data, File user_file){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(user_file, true));
            bw.write(data);
            bw.flush();
            bw.close();
            Intent redirect = new Intent(AddActivity.this, MainActivity.class);
            startActivity(redirect);

        }catch (IOException e){
            e.printStackTrace();
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
         dropdown = (Spinner)findViewById(R.id.spinner1);

         dropdown1 = (Spinner)findViewById(R.id.spinner2);
         dropdown2 = (Spinner)findViewById(R.id.spinner3);
         dropdown3 = (Spinner)findViewById(R.id.spinner4);
         dropdown4 = (Spinner)findViewById(R.id.spinner5);
         dropdown5 = (Spinner)findViewById(R.id.spinner6);
         dropdown6 = (Spinner)findViewById(R.id.spinner7);
         dropdown7 = (Spinner)findViewById(R.id.spinner8);
         dropdown8 = (Spinner)findViewById(R.id.spinner9);
        String[] items = new String[]{"01", "02", "03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        String[] items1 = new String[]{"01", "02", "03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        dropdown1.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();
        String[] items2 = new String[]{ "2017", "2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        String[] items3 = new String[]{"01", "02", "03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        dropdown3.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();
        String[] items4 = new String[]{"01", "02", "03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items4);
        dropdown4.setAdapter(adapter4);
        adapter4.notifyDataSetChanged();
        String[] items5 = new String[]{"2017", "2018","2019","2020","2021","2022","2023","2024","2025","2026","2027"};
        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items5);
        dropdown5.setAdapter(adapter5);
        adapter5.notifyDataSetChanged();
        String[] items6 = new String[]{"01", "02", "03","04","05","06","07","08","09","10","11","12"};
        ArrayAdapter<String> adapter6 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items6);
        dropdown6.setAdapter(adapter6);
        adapter6.notifyDataSetChanged();
        String[] items7 = new String[]{"00","05", "10","15","20","25","30","35","40","45","55"};
        ArrayAdapter<String> adapter7 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items7);
        dropdown7.setAdapter(adapter7);
        adapter7.notifyDataSetChanged();
        String[] items8 = new String[]{"am","pm"};
        ArrayAdapter<String> adapter8 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items8);
        dropdown8.setAdapter(adapter8);
        adapter8.notifyDataSetChanged();
        Button save_button = (Button) findViewById(R.id.save);
        save_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                AddActivity_user();
            }
        });
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
                Intent redirect = new Intent(AddActivity.this, LoginActivity.class);
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
