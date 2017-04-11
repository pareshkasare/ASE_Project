package com.example.ved.MARS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.ved.MARS.R.id.AutoMedName;

public class AddActivity extends AppCompatActivity implements TextWatcher {

    private String old_text="";
    private int s,fileName;
    private CustomAutoCompleteView textView;
    private ArrayAdapter<String> Autoadapter;
    private EditText dosage;
    private CheckBox sun,mon,wed,tue,thu,fri,sat;
    private Button save;
    private TextView textView5, textView6, textView7;
    //    private Spinner spinner2;
//    private Spinner spinner3;
//    private Spinner spinner1,spinner4,spinner5,spinner6,spinner7,spinner8,spinner9;
    private Spinner frommonth,fromdate,fromyear, tomonth,todate,toyear,hours,min,ampm;
    private boolean isInputValid = false;
    private boolean [] days = new boolean[]{false,false,false,false,false,false,false};
    String data;

    public void AddActivity_user(){
        System.out.println("in add user");
        textView =  (CustomAutoCompleteView)findViewById(AutoMedName);
        dosage = (EditText) findViewById(R.id.textView2);
        sun = (CheckBox) findViewById(R.id.checkbox1);
        mon = (CheckBox) findViewById(R.id.checkbox2);
        tue = (CheckBox) findViewById(R.id.checkbox3);
        wed = (CheckBox) findViewById(R.id.checkbox4);
        thu = (CheckBox) findViewById(R.id.checkbox5);
        fri = (CheckBox) findViewById(R.id.checkbox6);
        sat = (CheckBox) findViewById(R.id.checkbox7);


        String  Medname= textView.getText().toString();
        String Dosage = dosage.getText().toString();

        if (TextUtils.isEmpty(Medname)) {
            textView.setError(getString(R.string.error_field_required));
            textView.requestFocus();
        } else if (TextUtils.isEmpty(Dosage)) {
            dosage.setError(getString(R.string.error_field_required));
            dosage.requestFocus();
        }else if(!sun.isChecked() && !mon.isChecked() && !tue.isChecked() && !wed.isChecked() &&
                !thu.isChecked() && !fri.isChecked() && !sat.isChecked() ) {

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
            days[0] = sun.isChecked();
            days[1] = mon.isChecked();
            days[2] = tue.isChecked();
            days[3] = wed.isChecked();
            days[4] = thu.isChecked();
            days[5] = fri.isChecked();
            days[6] = sat.isChecked();
            System.out.println( "days: "+ Arrays.toString(days));

            data = Medname+":"+Dosage+":"+hours.getSelectedItem().toString()+":"+
                    min.getSelectedItem().toString()+":"+ampm.getSelectedItem().toString()+
                    ":"+Arrays.toString(days);
            System.out.println(data);
            //check if users file already present, if not then create one. If present then append data.
            //fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
            fileName = s+1;
            File user_file = new File(getApplicationContext().getFilesDir(), "Med"+fileName+".txt");
            if(!user_file.exists()){
                create_new_file(data,user_file);
            }

            setupNotification();

        }

    }
    private void setupNotification(){
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
        intent.putExtra("data",data+":Med"+fileName+".txt");

        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if(sun.isChecked()&&mon.isChecked()&&tue.isChecked()&&wed.isChecked()&&thu.isChecked()&&fri.isChecked()&&sat.isChecked())
        {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+10,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }
        else
        {
            if(sun.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,1);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+10,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(mon.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,2);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+20,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(tue.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,3);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+30,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(wed.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,4);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+40,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(thu.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,5);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+50,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(fri.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,6);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+60,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }
            if(sat.isChecked()){
                calendar.set(Calendar.DAY_OF_WEEK,7);
                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),fileName*100+70,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY*7,pendingIntent);
            }

        }
    }
    private void create_new_file(String data, File user_file){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(user_file, true));
            bw.write(data);
            System.out.print("file data: "+data);
            bw.flush();
            bw.close();
            Intent redirect = new Intent(AddActivity.this, MainActivity.class);
            startActivity(redirect);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        System.out.println("OnTextChanged." + old_text);

        // The below code block does:
        // When type a word, make a new ArrayAdapter and set it for textView
        // If any of word in suggestion list is clicked, nothing changes, dropdown list not shown.
        if (textView.isPerformingCompletion()) {
            // An item has been selected from the list. Ignore.
            return;
        }
        boolean check_API = true;

        if(old_text != null){
            if (old_text.length() < s.length()){
                check_API = true;
            }
            else{
                check_API = false;
            }
        }
        old_text = s.toString();
        if(check_API && s.length()>0){

            String getURL = "https://dailymed.nlm.nih.gov/dailymed/services/v2/rxcuis.json?rxString=" + s;
            System.out.println(getURL);
            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder()
                        .url(getURL)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("Error in call");
                        System.out.println(e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("onresponsesuccess");
                        final JSONObject jsonResult;
                        final String result = response.body().string();
                        System.out.println(result);
                        try {
                            jsonResult = new JSONObject(result);
                            final JSONArray dataArray= jsonResult.getJSONArray("data");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Autoadapter.clear();
                                    for(int i=0;i<dataArray.length();++i){
                                        try {
                                            JSONObject obj = dataArray.getJSONObject(i);
                                            Autoadapter.add(obj.getString("rxstring"));
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                    //Autoadapter.notifyDataSetChanged();
                                }
                            });

                            /*
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Autoadapter.add(mednames);
                                    Autoadapter.notifyDataSetChanged();
                                }
                            });*/

                            System.out.println();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    @Override
    public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
    }

    @Override
    public void afterTextChanged(final Editable s) {

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //s = getIntent().getStringExtra("lastfilenum");
        s = getIntent().getIntExtra("lastfilenum",0);
        //s=Integer.parseInt(str);
        setContentView(R.layout.activity_add);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
       /* frommonth = (Spinner)findViewById(R.id.spinner1);

        fromdate = (Spinner)findViewById(R.id.spinner2);
        fromyear = (Spinner)findViewById(R.id.spinner3);
        tomonth = (Spinner)findViewById(R.id.spinner4);
        todate = (Spinner)findViewById(R.id.spinner5);
        toyear = (Spinner)findViewById(R.id.spinner6);*/
        hours = (Spinner)findViewById(R.id.spinner7);
        min = (Spinner)findViewById(R.id.spinner8);
        ampm = (Spinner)findViewById(R.id.spinner9);

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
        Button save_button = (Button) findViewById(R.id.save);
        save_button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here
                AddActivity_user();
            }
        });

        textView = (CustomAutoCompleteView)findViewById(AutoMedName);
        textView.setHint("Enter medicine name");

        textView.setThreshold(1);
        textView.setLoadingIndicator(
                (android.widget.ProgressBar) findViewById(R.id.pb_loading_indicator));

        Autoadapter = new ArrayAdapter<String>(
                AddActivity.this,
                android.R.layout.simple_dropdown_item_1line);
        Autoadapter.setNotifyOnChange(true);
        textView.setAdapter(Autoadapter);
        textView.addTextChangedListener(this);
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
