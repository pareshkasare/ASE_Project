package com.example.ved.MARS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class UpdateActivity extends AppCompatActivity {

    private TextView medName;
    private EditText dosage;
    private Spinner hours,min,ampm;
    private CheckBox sun,mon,tue,wed,thu,fri,sat;
    private boolean[] days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        medName = (TextView) findViewById(R.id.medName);
        dosage  = (EditText) findViewById(R.id.dosage);
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

        String s = getIntent().getStringExtra("Extra_filename");
        System.out.println(s);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        File med = new File(getApplicationContext().getFilesDir(),s);
        try {
            BufferedReader br = new BufferedReader(new FileReader(med));
            String line;

            while ((line = br.readLine()) != null) {
                String[] pieces = line.split(":");
                System.out.println(line);
                medName.setText(pieces[0]);
                dosage.setText(pieces[1]);
                hours.setSelection(Arrays.asList(items6).indexOf(pieces[2]));
                min.setSelection(Arrays.asList(items7).indexOf(pieces[3]));
                ampm.setSelection(Arrays.asList(items8).indexOf(pieces[4]));
                String[] parts = pieces[5].substring(1,pieces[5].length()-1).split(", ");
                System.out.println("in $$$$$$$$$$$$$$$$$$" + pieces[5]);
                days = new boolean[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    System.out.println("in %%%%%%%%%%%%%%%%");
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
